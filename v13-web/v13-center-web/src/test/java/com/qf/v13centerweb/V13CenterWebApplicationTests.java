package com.qf.v13centerweb;

import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class V13CenterWebApplicationTests {

	//
	@Autowired
	private FastFileStorageClient client;

	@Test
	public void contextLoads() throws FileNotFoundException {
		//1 实现文件的上传
		File file = new File("G:\\myJavaItems\\IDEA\\v13\\v13-web\\v13-center-web\\1.png");
		FileInputStream fileInputStream1 = new FileInputStream(file);

		//StorePath storePath = client.uploadFile(fileInputStream1, file.length(), "html", null);
		StorePath storePath = client.uploadImageAndCrtThumbImage(fileInputStream1, file.length(), "png", null);
		System.out.println(storePath.getFullPath());
		System.out.println(storePath.getGroup());
		System.out.println(storePath.getPath());
	}

}
