package com.tywh.util;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.Inflater;
import java.io.BufferedInputStream; 
import java.io.BufferedOutputStream; 
import java.io.File; 
import java.io.InputStream; 
import java.io.OutputStream; 
  
import org.apache.commons.compress.archivers.ArchiveEntry; 
import org.apache.commons.compress.archivers.zip.Zip64Mode; 
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry; 
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream; 
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream; 


/**
 * 压缩和解压缩数据类
 * <p>Description:压缩和解压缩数据类  </p>
 * @version 1.0
 */

public class ZipUtils {
	/**
   * 压缩字符串
   * @param sourcestr String 需要压缩的字符串
   * @throws UnsupportedEncodingException, IOException, Exception
   * @return byte[]
   */
	public static byte[] zipString(String sourcestr) throws UnsupportedEncodingException, IOException, Exception {
		byte[] compressedData = null;
		if (sourcestr == null) {
			throw new Exception("无法压缩字符串，源字符串为 null!");
		}
		try {
			byte[] input = sourcestr.getBytes("GB2312");
			//以最高压缩率建立压缩对象
			Deflater compressor = new Deflater();
			compressor.setLevel(Deflater.BEST_COMPRESSION);
			//给出要压缩的字符
			compressor.setInput(input);
			compressor.finish();
			//根据输入字符的长度建立字节数组输出流
			ByteArrayOutputStream bos = new ByteArrayOutputStream(input.length);
			//压缩数据
			byte[] buf = new byte[1024];
			while (!compressor.finished()) {
				int count = compressor.deflate(buf);
				bos.write(buf, 0, count);
			}
			bos.close();
			//获得压缩后的字符
			compressedData = bos.toByteArray();
		} catch (UnsupportedEncodingException usee) {
			throw new UnsupportedEncodingException("不支持的字符集错误，相关信息如下：" + usee.getMessage());
		} catch (IOException ioe) {
			throw new IOException("输出流关闭错误，相关信息如下：" + ioe.getMessage());
		} catch (Exception e) {
			throw new Exception("压缩字符串错误，相关信息如下:" + e.getMessage());
		}
		return compressedData;
	}
	/**
   * 解压缩字符串
   * @param sourcebyte byte[] 需要解压缩的字节数组
   * @throws UnsupportedEncodingException, DataFormatException, Exception
   * @return String
   */
	public static String unzipString(byte[] sourcebyte) throws UnsupportedEncodingException, DataFormatException, Exception {
		String targetstr = "";
		String temp = "";
		if (sourcebyte == null) {
			throw new Exception("无法解压缩字符串，源字节数组为 null!");
		}
		try {
			Inflater decompresser = new Inflater();
			decompresser.setInput(sourcebyte);
			byte[] result = new byte[1024];
			while (!decompresser.finished()) {
				temp = "";
				int count = decompresser.inflate(result);
				temp = new String(result, 0, count, "GB2312");
				targetstr = targetstr + temp;
			}
			return targetstr;
		} catch (UnsupportedEncodingException usee) {
			throw new UnsupportedEncodingException("不支持的字符集错误，相关信息如下：" + usee.getMessage());
		} catch (DataFormatException dfe) {
			throw new DataFormatException("数据格式化错误，相关信息如下：" + dfe.getMessage());
		} catch (Exception e) {
			throw new Exception("不支持的字符集错误，相关信息如下：" + e.getMessage());
		}
	}
	//--------------需要进一步完善------------------//
	/**
   * 压缩指定路径下面的文件，并且将压缩后的文件按照用户指定的名称输出
   * @param sourceAddress String 需要压缩的文件地址
   * @param targetAddress String 输出压缩后文件的地址
   */
	public void gzipFile(String sourceAddress, String targetAddress) {
		try {
			//打开需压缩文件作为文件输入流
			FileInputStream fin = new FileInputStream(sourceAddress);
			//建立压缩文件输出流
			FileOutputStream fout = new FileOutputStream(targetAddress);
			//在文件输出流上建立gzip压缩输出流
			GZIPOutputStream gzout = new GZIPOutputStream(fout);
			byte[] buf = new byte[1024]; //设定读入缓冲区尺寸
			int num;
			while ((num = fin.read(buf)) != -1) {
				gzout.write(buf, 0, num);
			}
			gzout.close(); //!!!关闭流,必须关闭所有输入输出流.保证输入输出完整和释放系统资源.
			fout.close();
			fin.close();
		} catch (IOException e) {
		}
	}
	/**
   * 解压缩指定路径下面的文件，并且将解压缩后的文件按照用户指定的名称输出
   * @param sourceAddress String 需要解压缩的文件地址
   * @param targetAddress String 输出解压缩后文件的地址
   */
	public void ungzipFile(String sourceAddress, String targetAddress) {
		try {
			//建立gzip压缩文件输入流
			FileInputStream fin = new FileInputStream(sourceAddress);
			//建立gzip解压工作流
			GZIPInputStream gzin = new GZIPInputStream(fin);
			//建立解压文件输出流
			FileOutputStream fout = new FileOutputStream(targetAddress);
			byte[] buf = new byte[1024];
			int num;
			while ((num = gzin.read(buf, 0, buf.length)) != -1) {
				fout.write(buf, 0, num);
			}
			gzin.close();
			fout.close();
			fin.close();
		} catch (IOException e) {
		}
	}
	
	/** 
     * 把文件压缩成zip格式 
     * @param files         需要压缩的文件 
     * @param zipFilePath 压缩后的zip文件路径   ,如"D:/test/aa.zip"; 
     */
    public static void compressFiles2Zip(File[] files,String zipFilePath) { 
        if(files != null && files.length >0) { 
            if(isEndsWithZip(zipFilePath)) { 
                ZipArchiveOutputStream zaos = null; 
                try { 
                    File zipFile = new File(zipFilePath); 
                    zaos = new ZipArchiveOutputStream(zipFile); 
                    zaos.setUseZip64(Zip64Mode.AsNeeded); 
                    //将每个文件用ZipArchiveEntry封装 
                    //再用ZipArchiveOutputStream写到压缩文件中 
                    for(File file : files) { 
                        if(file != null) { 
                            ZipArchiveEntry zipArchiveEntry  = new ZipArchiveEntry(file,file.getName()); 
                            zaos.putArchiveEntry(zipArchiveEntry); 
                            InputStream is = null; 
                            try { 
                                is = new BufferedInputStream(new FileInputStream(file)); 
                                byte[] buffer = new byte[1024 * 5];   
                                int len = -1; 
                                while((len = is.read(buffer)) != -1) { 
                                    //把缓冲区的字节写入到ZipArchiveEntry 
                                    zaos.write(buffer, 0, len); 
                                } 
                                //Writes all necessary data for this entry. 
                                zaos.closeArchiveEntry();   
                            }catch(Exception e) { 
                                throw new RuntimeException(e); 
                            }finally { 
                                if(is != null)  
                                    is.close(); 
                            } 
                              
                        } 
                    } 
                    zaos.finish(); 
                }catch(Exception e){ 
                	System.out.println("生成压缩包异常"+e.getMessage());
                    throw new RuntimeException(e); 
                }finally { 
                        try { 
                            if(zaos != null) { 
                                zaos.close(); 
                            } 
                        } catch (IOException e) { 
                        	System.out.println("生成压缩包运行时异常"+e.getMessage());
                            throw new RuntimeException(e); 
                        } 
                } 
                  
            } 
              
        } 
          
    } 
      
    /** 
     * 把zip文件解压到指定的文件夹 
     * @param zipFilePath   zip文件路径, 如 "D:/test/aa.zip" 
     * @param saveFileDir   解压后的文件存放路径, 如"D:/test/" 
     */
    public static void decompressZip(String zipFilePath,String saveFileDir) { 
        if(isEndsWithZip(zipFilePath)) { 
            File file = new File(zipFilePath); 
            if(file.exists()) { 
                InputStream is = null; 
                //can read Zip archives 
                ZipArchiveInputStream zais = null; 
                try { 
                    is = new FileInputStream(file); 
                    zais = new ZipArchiveInputStream(is); 
                    ArchiveEntry  archiveEntry = null; 
                    //把zip包中的每个文件读取出来 
                    //然后把文件写到指定的文件夹 
                    while((archiveEntry = zais.getNextEntry()) != null) { 
                        //获取文件名 
                        String entryFileName = archiveEntry.getName(); 
                        //构造解压出来的文件存放路径 
                        String entryFilePath = saveFileDir + entryFileName; 
                        byte[] content = new byte[(int) archiveEntry.getSize()]; 
                        zais.read(content); 
                        OutputStream os = null; 
                        try { 
                            //把解压出来的文件写到指定路径 
                            File entryFile = new File(entryFilePath); 
                            os = new BufferedOutputStream(new FileOutputStream(entryFile)); 
                            os.write(content); 
                        }catch(IOException e) { 
                            throw new IOException(e); 
                        }finally { 
                            if(os != null) { 
                                os.flush(); 
                                os.close(); 
                            } 
                        } 
                          
                    } 
                }catch(Exception e) { 
                    throw new RuntimeException(e); 
                }finally { 
                        try { 
                            if(zais != null) { 
                                zais.close(); 
                            } 
                            if(is != null) { 
                                is.close(); 
                            } 
                        } catch (IOException e) { 
                            throw new RuntimeException(e); 
                        } 
                } 
            } 
        } 
    } 
      
    /** 
     * 判断文件名是否以.zip为后缀 
     * @param fileName        需要判断的文件名 
     * @return 是zip文件返回true,否则返回false 
     */
    public static boolean isEndsWithZip(String fileName) { 
        boolean flag = false; 
        if(fileName != null && !"".equals(fileName.trim())) { 
            if(fileName.endsWith(".ZIP")||fileName.endsWith(".zip")){ 
                flag = true; 
            } 
        } 
        return flag; 
    } 
    
    
    public void testCompressFiles2Zip() { 
        //存放待压缩文件的目录 
        File srcFile = new File("D:/test"); 
        //压缩后的zip文件路径 
        String zipFilePath = "d:/test2/test.zip"; 
        if(srcFile.exists()) { 
            File[] files = srcFile.listFiles(); 
            compressFiles2Zip(files, zipFilePath); 
            System.out.println("生成压缩包成功");
        } 
    } 
      
    public void testDecompressZip()  { 
        //压缩包所在路径 
        String zipFilePath = "d:/test2/test.zip"; 
        //解压后的文件存放目录 
        String saveFileDir = "d:/test2/"; 
        //调用解压方法 
       decompressZip(zipFilePath, saveFileDir); 
    } 
    
}
