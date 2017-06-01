package com.tywh.action;

import javax.servlet.http.HttpServlet;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
/**
 *	Upload
 *  author：杜泉
 *  2014-7-25 下午2:25
 */
public class Upload extends HttpServlet
{
	private static final long serialVersionUID = 1L;

	//上传文件的保存路径
	protected String dirTemp = "\\temp\\";
	
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		 this.doPost(request, response);
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html; charset=UTF-8");
		PrintWriter out = response.getWriter();
		String folder = request.getParameter("folder");
		//folder=new String(folder.getBytes("ISO-8859-1"),"UTF-8");
		folder=URLDecoder.decode(folder, "UTF-8"); 
		folder.replace("/", "\\");
		//文件保存目录路径
		String savePath = this.getServletContext().getRealPath("\\upload\\") +"\\"+folder;
		// 临时文件目录 
		String tempPath = this.getServletContext().getRealPath("\\upload\\") +"\\" +dirTemp;

		//创建文件夹
		File dirFile = new File(savePath);
		if (!dirFile.exists()) {
			dirFile.mkdirs();
		}
		
		//创建临时文件夹
		File dirTempFile = new File(tempPath);
		if (!dirTempFile.exists()) {
			dirTempFile.mkdirs();
		}
		
		DiskFileItemFactory  factory = new DiskFileItemFactory();
		factory.setSizeThreshold(200 * 1024 * 1024); //设定使用内存超过5M时，将产生临时文件并存储于临时目录中。   
		factory.setRepository(new File(tempPath)); //设定存储临时文件的目录。   

		ServletFileUpload upload = new ServletFileUpload(factory);
		upload.setHeaderEncoding("UTF-8");
		try {
			List items = upload.parseRequest(request);
			Iterator itr = items.iterator();
			
			while (itr.hasNext()) {
				FileItem item = (FileItem) itr.next();
				String fileName = item.getName();
				if (!item.isFormField()) {
					try{
						File uploadedFile = new File(savePath, fileName);
	                    OutputStream os = new FileOutputStream(uploadedFile);
	                    InputStream is = item.getInputStream();
	                    byte buf[] = new byte[10240000];//可以修改 1024 以提高读取速度
	                    int length = 0;  
	                    while( (length = is.read(buf)) > 0 ){  
	                        os.write(buf, 0, length);  
	                    }  
	                    //关闭流  
	                    os.flush();
	                    os.close();  
	                    is.close();  
	                    System.out.println("上传成功！路径："+savePath+fileName);
					}catch(Exception e){
						e.printStackTrace();
					}
				}			
			} 
			
		} catch (FileUploadException e) {
			e.printStackTrace();
		}
		out.flush();
		out.close();
	}
}

