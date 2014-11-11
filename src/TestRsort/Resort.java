package TestRsort;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Vector;

import javax.swing.JOptionPane;

public class Resort {
	public void resortTotemp(File oldFile, String tempPath)throws IOException
	{
		Vector vector = new Vector();
		boolean IsRepeat = false;
		String string = null;
		String vString = null;
		try {
			BufferedReader bReader = new BufferedReader(new FileReader(oldFile));
			while((string=bReader.readLine())!=null)
			{
				for(int i = 0;i < vector.size();i++)
				{
					vString = (String) vector.elementAt(i);
					if(vString.equals((string.trim())))
					{
						IsRepeat = true;
						break;
					}
				}
				if(IsRepeat)
				{
					IsRepeat = false;
				}else {
					vector.add(string.trim());
				}
			}
			bReader.close();
		} catch (IOException e) {
			e.printStackTrace();
			// TODO: handle exception
		}
		File tempFile = new File(tempPath);
		if(!tempFile.exists()){
			tempFile.createNewFile();
		}
		BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile,true));
		for(int i = 0;i < vector.size(); i++)
		{
			string = (String) vector.elementAt(i);
			writer.append(string);
			writer.newLine();
		}
	}
	public void AllResortTotemp(String fatherFile,String tempFile) throws IOException
	{
		File file = new File(fatherFile);
		File[]files = file.listFiles();
		for(File aFile : files)
		{
			resortTotemp(aFile, tempFile);
		}
	}
    public void DelTemp(String tempPath)throws IOException{  
        try{  
        File tempFile = new File(tempPath);  
        if(!tempFile.exists()){  
            JOptionPane.showMessageDialog(null, "没有生成temp.txt文件");  
            return;  
        }  
          
            tempFile.delete();  
        }catch(Exception e){  
            e.printStackTrace();  
        }  
    }  
	public static void main(String[] args) throws IOException{
		String fatherFile = "F:\\WeiboData\\emotion";
		String tempPath = "F:\\WeiboData\\temp.txt";
		File tempFile = new File(tempPath);
		String resultPath = "F:\\WeiboData\\result.txt";
		Resort resort = new Resort();
		resort.AllResortTotemp(fatherFile, tempPath);
		resort.resortTotemp(tempFile, resultPath);
        resort.DelTemp(tempPath);//删除temp.txt中转文件  
        System.out.println("OK");  
	}
}
