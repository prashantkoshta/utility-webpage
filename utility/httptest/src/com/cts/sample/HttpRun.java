package com.cts.sample;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;

public class HttpRun {

	/**
	 * @param args
	 */
	private String basePath;
	private String sourcePath;
	private String urlFilePath;
	private String jsSeprator;
	private String mainFileName;
	
	public static void main(String[] args){
		System.out.println("Starting....");
		HttpRun m = new HttpRun();
		m.basePath =args[0];
		m.sourcePath = args[1];
		m.urlFilePath = args[2];
		m.jsSeprator = args[3];
		m.mainFileName = args[4];
//		m.basePath ="D:\\Temp";
//		m.sourcePath = "D:\\111";
//		m.urlFilePath = "D:\\111\\pages-link_$$$$$$.txt";
//		m.jsSeprator = "_$$$";
		ArrayList<String> lines = m.readURLFile(m.urlFilePath);
		if(!lines.equals(null)){
			for (int i = 0; i < lines.size(); i++) {
				m.parseUrl(lines.get(i));
			}
		}
		//m.parseUrl("http://dfsstaging.cognizant.com/acnav/images/12/jquery-1.6.1.min.js_$$$");
		m.mainFileMove(m.sourcePath,m.basePath, m.mainFileName);
		System.out.println("Completed....File Loaction : "+m.basePath);
		
	
	}
	
	private void parseUrl(String s){
		int i=0;
		String path ="";
		String fileName = "";
		try {
			URL url = new URL(s);
			s = url.getPath();
			String filenamse[]= s.split("/");
			while(i<filenamse.length){
				if(!filenamse[i].equals("")){
					if(filenamse[i].indexOf(".")>0){
						int end = s.indexOf("/",s.indexOf(filenamse[i])+filenamse[i].length());
//						System.out.println(end);
						if(end<0){
							fileName = filenamse[i];
						}else{
							path+="//"+filenamse[i];
						}
					}else{
						path+="//"+filenamse[i];
					}
				}
				i++;
			}
//			System.out.println(path);
//			System.out.println(fileName);
			
			if(!path.equals("")){
				createDirectory(path);
			}
			
			if(!fileName.equals("")){
				moveFile(this.sourcePath,this.basePath+path,fileName);
			}
			
			
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}
	
	private void createDirectory(String path){
		File files = new File(this.basePath+path);
		if (!files.exists()) {
			if (files.mkdirs()) {
			} else {
				System.out.println("Failed to create multiple directories!");
			}
		}
	}
	
	private void moveFile(String sourcePath,String targetPath,String fileName){
        Path movefrom = FileSystems.getDefault().getPath(sourcePath+"\\"+fileName);
        String fileNewName = "";
        if(fileName.lastIndexOf(jsSeprator)>-1){
        	fileNewName = fileName;
        	fileNewName = fileNewName.substring(0,fileName.lastIndexOf(jsSeprator));
        }
        Path target = FileSystems.getDefault().getPath(targetPath+"\\"+fileName);
        try {
        	 Files.move(movefrom, target, StandardCopyOption.REPLACE_EXISTING);
        	 if(!fileNewName.equals("")){
        		 File presentFile = new File(targetPath+"\\"+fileName);
            	 File newFileName = new File(targetPath+"\\"+fileNewName);
            	 presentFile.renameTo(newFileName);
        	 }
        	
        } catch (IOException e) {
            System.err.println(e);
        }
	}
	
	private void mainFileMove(String sourcePath,String targetPath,String fileName){
        Path movefrom = FileSystems.getDefault().getPath(sourcePath+"\\"+fileName);
        String fileNewName = fileName;
        Path target = FileSystems.getDefault().getPath(targetPath+"\\"+fileName);
        try {
        	 Files.move(movefrom, target, StandardCopyOption.REPLACE_EXISTING);
        	 if(!fileNewName.equals("")){
        		 File presentFile = new File(targetPath+"\\"+fileName);
            	 File newFileName = new File(targetPath+"\\"+fileNewName);
            	 presentFile.renameTo(newFileName);
        	 }
        	
        } catch (IOException e) {
            System.err.println(e);
        }
	}
	
	private ArrayList<String> readURLFile(String sourcePath){
		Path path = Paths.get(sourcePath);
		Charset charset = Charset.forName("UTF-8");
		String line;
		ArrayList<String> lines = new ArrayList<String>();
		try (BufferedReader reader = Files.newBufferedReader(path , charset)) {
		  while ((line = reader.readLine()) != null ) {
			  lines.add(line);
		  }
		} catch (IOException e) {
		    System.err.println(e);
		}
		return lines;
	}

}
