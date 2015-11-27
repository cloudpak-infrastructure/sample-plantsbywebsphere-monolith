package org.pbw.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class AppTest {
	
	static String wasHome = "";
	static String appToInstall = "";
	
	@BeforeClass
	public static void setUpTest() throws IOException {
		wasHome = System.getProperty("was.test.home");
		appToInstall = System.getProperty("app.location");
		
		
		File outLog = new File(wasHome, "logs/server1/SystemOut.log");
		
		BufferedReader br = new BufferedReader(new FileReader(outLog));
		while (br.readLine() != null) {}
		File app = new File(appToInstall);
		
		File wasMonitoredDir = new File(wasHome, "monitoredDeployableApps/servers/server1");
		FileUtils.copyFileToDirectory(app, wasMonitoredDir);
		
		
		String line = br.readLine();
		while (true) {
			if (line == null) {
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				
			} else if (line.contains("CWLDD0008I")) {break;
			}

			line = br.readLine();
		}
		
		
		br.close();
	}

    /**
     * Rigourous Test :-)
     */
	@Test
    public void testApp()
    {
		
        
    }
}
