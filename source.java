import java.util.*;
import java.io.*;
class source{

	public static void main(String args[])
	{
		int k;
		String name="trunk";
		name=clone_repository();           //will clone repository and create log.
		String name=clone_repository();
		create_log(name);
		find_commits(name);               //find the relevant commit ids and save them in a file.
	    k=find_diff(name);
	    mining_diff(9,name);
	}

	public static String clone_repository()
	{
		Scanner s= new Scanner(System.in);
		System.out.println("Enter the link of SVN repository:");
		String link = s.nextLine();
		System.out.println("Enter name of the repository:");
		String name =s.nextLine();
		replaceSelected(link,name);
		clone_cmd();    //will run command line in background.
		create_log(name);
		return name;
	}

	public static void replaceSelected(String link, String name) {
	    try {
	        BufferedReader file = new BufferedReader(new FileReader("clone.bat"));
	        String line;
	        int counter=0;
	        String input = "";
	        while ((line = file.readLine()) != null)
	        	{
	        	if(counter==4)
	        	input+= "svn checkout " + link + '\n';
	        	else if(counter==5)
	        	input+= "cd " + name;
	        	else
	        	input += line + '\n';
	        	counter++;
	        	}
	        file.close();
	        //System.out.println(input); // check that it's inputed right
	        FileOutputStream fileOut = new FileOutputStream("clone.bat");
	        fileOut.write(input.getBytes());
	        fileOut.close();
	        //System.out.print("cloning  success");
	    } catch (Exception e) {
	        System.out.println("Problem reading file.");
	    }
	}

	public static void clone_cmd()
	{
		try{
			Runtime.getRuntime().exec( "C:\\Windows\\System32\\cmd.exe /c start /wait clone.bat");
		}catch (Exception e) {
	        System.out.println("Exception while opening command line");
		}
	}

	public static void create_log(String name)
	{
		 try {
		        BufferedReader file = new BufferedReader(new FileReader("createlog.bat"));
		        String line;
		        int counter=1;
		        String input = "";
		        while ((line = file.readLine()) != null)
		        	{
		        	if(counter==3)
		        	input+= "cd " + name + '\n';
		        	else
		        	input += line + '\n';
		        	counter++;
		        	}
		        file.close();
		        //System.out.println(input); // check that it's inputed right
		        FileOutputStream fileOut = new FileOutputStream("createlog.bat");
		        fileOut.write(input.getBytes());
		        fileOut.close();
		        //System.out.print("cloning  success");
		    } catch (Exception e) {
		        System.out.println("Problem reading file in create log.");
		    }


		try{
		Process p=	Runtime.getRuntime().exec( "C:\\Windows\\System32\\cmd.exe /c start /wait createlog.bat");
		int exitVal = p.waitFor();
		}catch (Exception e) {
	        System.out.println("Exception while opening command line");
		}
	}

	public static void find_commits(String source)
	{
		char[] ids= new char[50001];
        char[] id= new char[10];
        int n=0;
		try {
			String path;
			path="C:\\Users\\Public\\svn_r\\"+source+"\\log.txt";
	        BufferedReader file = new BufferedReader(new FileReader(path));
	        String line;
	        boolean flag=false;
	        int i,j,k=0,e,l;
	        String input = "";
	        while ((line = file.readLine()) != null)
	        	{
	              i=line.indexOf("------");
	              if(i!=-1)
	              {
	            	  flag=true;
	            	  continue;
	              }
	              if(flag==true)
	              {
	            	  line.getChars(0, 8, id, 0);
                      flag=false;
	              }
	        	  i=line.indexOf("Fix"); j=line.indexOf("fix"); e=line.indexOf("error");
	        	  l=line.indexOf("Error");
	        	  if((i!=-1 || j!=-1 )&& id[0]!='t')
	        	  {
	        		 k++;
	        		 ids[n]='r';
	        		 n++;
	                for(j=1;id[j]<=57 && id[j]>=48;j++)
	                {
	                	ids[n]=id[j];
	                	n++;
	                }
	                id[0]='t';
	        	  }
	        	}
	        file.close();
	   //     System.out.println(k);
	    } catch (Exception e) {
	        //System.out.println("Problem reading file in find commits.");
	    	System.out.print("\n");
	    }
		//System.out.print(ids);
		write_to_file(ids,n);
	}

	public static void write_to_file(char ids[],int n)
	{
		int i=0;
		try {
	        BufferedWriter file = new BufferedWriter(new FileWriter("ids.txt"));
	        String input = "";
	        while (i<n)
	        	{
	        	 input+=ids[i];
	             i++;
	             if(ids[i]=='r')
	             input+="\n";
	        	}
	        file.close();
	        FileOutputStream fileOut = new FileOutputStream("ids.txt");
	        fileOut.write(input.getBytes());
	        fileOut.close();
	        //System.out.println(k);
	    } catch (Exception e) {
	        System.out.println("Problem writing ids.");
	    }
	}

	public static int find_diff(String name)
	{
		try {
	        BufferedReader file = new BufferedReader(new FileReader("creatediff.bat"));
	        String line;
	        int counter=1;
	        String input = "";
	        while ((line = file.readLine()) != null)
	        	{
	        	if(counter==3)
	        	input+= "cd " + name + '\n';
	        	else
	        	input += line + '\n';
	        	counter++;
	        	}
	        file.close();
	        FileOutputStream fileOut = new FileOutputStream("creatediff.bat");
	        fileOut.write(input.getBytes());
	        fileOut.close();
	        //System.out.print("cloning  success");
	    } catch (Exception e) {
	        System.out.println("Problem reading file in find_diff.");
	    }
		String id1=null,id2=null;
		int diff_no=0;

		try {
	        BufferedReader file = new BufferedReader(new FileReader("ids.txt"));
	        String line;
	        while ((line = file.readLine()) != null)
	        	{
	             if(id1==null && id2==null)
	             {
	            	 id1=line;
	             }
	             else if(id1!=null && id2==null)
	             {
	            	 id2=line;
	             }
	             else if(id2!=null && id1!=null)
	             {
	            	 diff_no++;
	            	 //System.out.print(id1+" ");
	            	 //System.out.println(id2);
	            	 run_diff(id1,id2,diff_no);
	            	 id1=id2;
	            	 id2=line;
	             }
	        	}
	        file.close();
	    } catch (Exception e) {
	        System.out.println("Problem reading file ids.");
	    }
		try{
		Process p=Runtime.getRuntime().exec("C:\\Windows\\System32\\cmd.exe /c start /wait creatediff.bat");
		int exitVal = p.waitFor();
		}catch (Exception e) {
	        System.out.println("Exception while opening command line");
		}
		 return diff_no;
	}

	public static void run_diff(String id1,String id2,int diff_no)
	{
		try{
			FileWriter fw = new FileWriter("creatediff.bat",true);
			fw.write("\nsvn diff -"+id1+":"+id2+" > diff"+diff_no+".txt");
	        fw.close();
		}
		catch (Exception e){
			System.out.println("Problem reading batch file create diff.");
		}
	}


	public static void mining_diff(int k,String source)
	{
		String input2=" ",name=" ";
		int i;
		for(i=1;i<=k;i++)
		{
			String path;
			path="C:\\Users\\Public\\svn_r\\"+source+"\\diff"+i+".txt";
			int j,idx,idx2;
			boolean flag1,flag2;
			try {
		        BufferedReader file = new BufferedReader(new FileReader(path));
		        String line;
		        flag1=false;flag2=false;
		        String input="Name ,Lines Deleted  ,Lines Added  ,Bug Fixed  , Changed,";
		        input+="LOC  ,LCOM   , WMC1  ,WMCv  ,DIT  ,NOC  ,CBO  \n";
		        while ((line = file.readLine()) != null)
		        	{
		        	 j=line.indexOf("======================");
		              if(j!=-1)
		              {
		            	  flag1=true;
		            	  continue;
		              }
		              idx=line.indexOf("+++");
		              idx2=line.indexOf(".java");
		              if(idx!=-1 && idx2!=-1)
		               {
		            		flag2=true;
		            		name=line.substring(3,line.indexOf(".java")+ 5);
		            		input2=findc_m(name,source);
		                    input+=name;
		            		continue;
		               }
		              if(flag1==true && flag2==true)
		              {
		            	    idx=line.indexOf("@@ -");
			            	if(idx!=-1)
			            	{
			            		String x,y;
			            		x=line.substring(line.indexOf(","), line.indexOf("+"));
			            		y=line.substring(line.lastIndexOf(","), line.lastIndexOf("@@"));
			            		input+=x+y+",yes,yes,";
			            		input=input+input2;
			            		flag2=false;
			            		flag1=false;
			            	}
		              }
		        	}
		        file.close();
		        //System.out.print(input);
		        path="diff"+i+".csv";
		        FileOutputStream fileOut = new FileOutputStream(path);
		        fileOut.write(input.getBytes());
		        fileOut.close();
		    } catch (Exception e) {
		        System.out.println("Problem reading file in mining diff.");
		    }		//end of catch
		}  //end of for
	}  //end of function
   public static String findc_m(String fname,String source)
   {
	   String path=source+"/";
	   path=path+fname.substring(1);
	   try {
	        BufferedReader file = new BufferedReader(new FileReader("cccc.bat"));
	        String line;
	        int counter=1;
	        String input = "";
	        while ((line = file.readLine()) != null)
	        	{
	        	if(counter==3)
	        	input+= "cccc " +path+"\n";
	        	else
	        	input += line + '\n';
	        	counter++;
	        	}
	        file.close();
	        FileOutputStream fileOut = new FileOutputStream("cccc.bat");
	        fileOut.write(input.getBytes());
	        fileOut.close();
	    } catch (Exception e) {
	        System.out.println("Problem reading file in find c and m.");
	    }
	    try{
			Process p=Runtime.getRuntime().exec("C:\\Windows\\System32\\cmd.exe /c start /wait cccc.bat");
			int exitVal = p.waitFor();
			}catch (Exception e) {
		        System.out.println("Exception while opening command line");
			} // end of catch
	    String result=mining_cccc();
		return result;
   }  //end of function

   public static String mining_cccc()
   {
	   String input="";
	   int wmc=0,wmcv=0,dit=-1,noc=0,cbo=-1,x,idx,idx2,counter=0;
	   String path="C:\\Users\\Public\\svn_r\\.cccc\\cccc.xml";
	   boolean val,flag;
	   try {
	        BufferedReader file = new BufferedReader(new FileReader(path));
	        String line;
	        val=false;flag=false;
	        while ((line = file.readLine()) != null)
	        {
	          idx=line.indexOf("project_summary");
	          if(idx!=-1 && val==false)
	          {
	           val=true;
	           continue;
	          }
	          if(val==true)
	          {
	        	  idx2=line.indexOf("lines_of_code value");
	        	  if(idx2!=-1)
	        	  {
	        		  input+=line.substring(line.indexOf("value")+7, line.indexOf("level")-2);
	        		  input+=",";
	        	  }
	        	  idx2=line.indexOf("lines_of_comment value");
	        	  if(idx2!=-1)
	        	  {
	        		  input+=line.substring(line.indexOf("value")+7, line.indexOf("level")-2);
	        		  input+=",";
	        	  }
	          }
	          idx=line.indexOf("</project_summary>");
	          if(idx!=-1)
	        	 val=false;

	            idx=line.indexOf("oo_design");
	            if(idx!=-1 && flag==false)
	            {
	            flag=true;
	            counter=0;
	            continue;
	            }
	            if(flag==true)
	            {
	            	counter++;
	            	if(counter==8)
	            	{
	            		counter=0;
	            		continue;
	            	}
	            	if(counter==3)
	            	{
	            	x=Integer.parseInt(line.substring(line.indexOf("=")+2, line.indexOf("level")-2));
	            	wmc+=x;
	            	}
	            	if(counter==4)
	            	{
	            	x=Integer.parseInt(line.substring(line.indexOf("=")+2, line.indexOf("level")-2));
		            wmcv+=x;
	            	}
	            	if(counter==5)
	            	{
	            	x=Integer.parseInt(line.substring(line.indexOf("=")+2, line.indexOf("level")-2));
		            if(dit<x)
		            	dit=x;
	            	}
	            	if(counter==6)
	            	{
	            	x=Integer.parseInt(line.substring(line.indexOf("=")+2, line.indexOf("level")-2));
		            noc+=x;
	            	}
	            	if(counter==7)
	            	{
	            	x=Integer.parseInt(line.substring(line.indexOf("=")+2, line.indexOf("level")-2));
		            if(cbo<x)
		            cbo=x;
	            	}
	            }
	            idx=line.indexOf("/oo_design");
	            if(idx!=-1)
	            {
	            flag=false;
	            break;
	            }
	        }
	        input+=Integer.toString(wmcv);
	        input+=",";
	        if(dit==-1)
	        	dit=0;
	        input+=Integer.toString(dit);
	        input+=",";
	        input+=Integer.toString(noc);
	        input+=",";
	        if(cbo==-1)
	        	cbo=0;
	        input+=Integer.toString(cbo);
	        input+="\n";
	      file.close();
	    } catch (Exception e) {
	        System.out.println("Problem reading file in mining cccc.");
	    }
	   return input;
   }  //end of function
}   //end of code

