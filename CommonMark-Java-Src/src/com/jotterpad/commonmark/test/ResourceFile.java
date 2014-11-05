package com.jotterpad.commonmark.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import org.junit.rules.ExternalResource;

public class ResourceFile extends ExternalResource {

	private String _res;
	private File _file = null;
	private InputStream _stream;

	public ResourceFile(String res) {
		this._res = res;
	}

	public File getFile() throws IOException {
		if (_file == null) {
			createFile();
		}
		return _file;
	}

	public InputStream getInputStream() {
		return _stream;
	}

	public InputStream createInputStream() {
		return getClass().getResourceAsStream(_res);
	}

	public String getContent() throws IOException {
		return getContent("utf-8");
	}

	public String getContent(String charSet) throws IOException {
//		InputStream inputStream = new FileInputStream(_res);
//		InputStreamReader reader = new InputStreamReader(inputStream,"utf-8");
		InputStreamReader reader = new InputStreamReader(createInputStream(),
				Charset.forName(charSet));
		char[] tmp = new char[4096];
		StringBuilder b = new StringBuilder();
		try {
			while (true) {
				int len = reader.read(tmp);
				if (len < 0) {
					break;
				}
				b.append(tmp, 0, len);
			}
			reader.close();
		} catch (IOException e) {
		} finally {
			reader.close();
		}
		return b.toString();
	}

	@Override
	protected void before() throws Throwable {
		super.before();
		_stream = getClass().getResourceAsStream(_res);
	}

	@Override
	protected void after() {
		try {
			if (_stream != null)
				_stream.close();
		} catch (IOException e) {
			// ignore
		}
		if (_file != null) {
			_file.delete();
		}
		super.after();
	}

	private void createFile() throws IOException {
		_file = new File(".", _res);
		InputStream stream = getClass().getResourceAsStream(_res);
		try {
			_file.createNewFile();
			FileOutputStream ostream = null;
			try {
				ostream = new FileOutputStream(_file);
				byte[] buffer = new byte[4096];
				while (true) {
					int len = stream.read(buffer);
					if (len < 0) {
						break;
					}
					ostream.write(buffer, 0, len);
				}
			} finally {
				if (ostream != null) {
					ostream.close();
				}
			}
		} finally {
			stream.close();
		}
	}

}