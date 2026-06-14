/*
 * Copyright (c) Koala 2012-2014 All Rights Reserved
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.openkoala.koala.monitor.support;

import java.io.File;
import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang.time.DateFormatUtils;
import org.openkoala.koala.monitor.model.ServerStatusVo;
import org.openkoala.koala.monitor.model.ServerStatusVo.CpuInfoVo;
import org.openkoala.koala.monitor.model.ServerStatusVo.DiskInfoVo;

import com.sun.management.OperatingSystemMXBean;

/**
 * 
 * 功能描述：服务器状态采集工具<br />
 *  
 * 创建日期：2013-6-19 上午9:17:40  <br />   
 * 
 * 版权信息：Copyright (c) 2013 Koala All Rights Reserved<br />
 * 
 * 作    者：<a href="mailto:vakinge@gmail.com">vakin jiang</a><br />
 * 
 * 修改记录： <br />
 * 修 改 者    修改日期     文件版本   修改说明
 */
public class ServerStatusCollector {
	
	//磁盘读写初始数据 用于计算读写速率
	private static Map<String, String> diskWritesAndReadsOnInit = new HashMap<String, String>();
	private static long initTime;
	private static final long MB = 1024L * 1024L;
	static{
		initTime = System.currentTimeMillis();
	}

	/**
	 * 获取服务器状态信息
	 * @return
	 */
	public static ServerStatusVo getServerAllStatus(){
		ServerStatusVo status = new ServerStatusVo();
		try {
			getServerBaseInfo(status);
			getServerCpuInfo(status);
			getServerDiskInfo(status);
			getServerMemoryInfo(status);
		} catch (Exception e) {
			return status;
		}
		return status;
	}
	
	/**
	 * 获取服务器基本信息
	 * @return
	 */
	public static void getServerBaseInfo(ServerStatusVo status){
		status.setServerTime(DateFormatUtils.format(Calendar.getInstance(), "yyyy-MM-dd HH:mm:ss"));
		status.setServerName(getServerName());
//		status.setJavaServer(RuntimeContext.getContext().getServerName());
//		status.setDeployPath(RuntimeContext.getContext().getDeployPath());
		
		Runtime rt = Runtime.getRuntime();
		status.setJvmTotalMem(rt.totalMemory()/MB);
		status.setJvmFreeMem(rt.freeMemory()/MB);
		
		Properties props = System.getProperties();
		status.setServerOs(props.getProperty("os.name") + " " + props.getProperty("os.arch") + " " + props.getProperty("os.version"));
		status.setJavaHome(props.getProperty("java.home"));
		status.setJavaVersion(props.getProperty("java.version"));
		status.setJavaTmpPath(props.getProperty("java.io.tmpdir"));

	}
	
	private static String getServerName() {
		String name = System.getenv().get("COMPUTERNAME");
		if (name == null || name.trim().length() == 0) {
			name = System.getenv().get("HOSTNAME");
		}
		if (name == null || name.trim().length() == 0) {
			try {
				name = InetAddress.getLocalHost().getHostName();
			} catch (Exception e) {
				name = "localhost";
			}
		}
		return name;
	}
	
	public static void getServerCpuInfo(ServerStatusVo status){
		try {
			java.lang.management.OperatingSystemMXBean standardBean = ManagementFactory.getOperatingSystemMXBean();
			OperatingSystemMXBean osBean = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);
			int processors = Math.max(1, standardBean.getAvailableProcessors());
			double cpuLoad = -1D;
			if (osBean != null) {
				cpuLoad = osBean.getSystemCpuLoad();
			}
			if (cpuLoad < 0D) {
				double loadAverage = standardBean.getSystemLoadAverage();
				cpuLoad = loadAverage >= 0D ? loadAverage / processors : 0D;
			}
			cpuLoad = Math.max(0D, Math.min(1D, cpuLoad));

			CpuInfoVo cpuInfo = new CpuInfoVo();
			cpuInfo.setCacheSize(0L);
			cpuInfo.setModel(standardBean.getArch());
			cpuInfo.setTotalMHz(0);
			cpuInfo.setVendor(System.getProperty("java.vendor"));
			cpuInfo.setUsed(cpuLoad * processors);
			cpuInfo.setIdle((1D - cpuLoad) * processors);
			cpuInfo.setCoreCount(processors);
			status.getCpuInfos().add(cpuInfo);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
    public static void getServerMemoryInfo(ServerStatusVo status){
		try {
			OperatingSystemMXBean osBean = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);
			long totalMem = osBean == null ? 0L : osBean.getTotalPhysicalMemorySize();
			long freeMem = osBean == null ? 0L : osBean.getFreePhysicalMemorySize();
			if (totalMem <= 0L) {
				Runtime rt = Runtime.getRuntime();
				totalMem = rt.totalMemory();
				freeMem = rt.freeMemory();
			}
			status.setTotalMem(totalMem/MB);
			status.setFreeMem(freeMem/MB);
			status.setUsedMem((totalMem - freeMem)/MB);
			//交换区
			long totalSwap = osBean == null ? 0L : osBean.getTotalSwapSpaceSize();
			long freeSwap = osBean == null ? 0L : osBean.getFreeSwapSpaceSize();
			status.setTotalSwap(totalSwap/MB);
			status.setFreeSwap(freeSwap/MB);
			status.setUsedSwap((totalSwap - freeSwap)/MB);
		} catch (Exception e) {

		}
	}
    
    
    public static void getServerDiskInfo(ServerStatusVo status){
		try {
			File[] roots = File.listRoots();
			if (roots == null || roots.length == 0) {
				roots = new File[] {new File(File.separator)};
			}
			for (int i = 0; i < roots.length; i++) {
				File root = roots[i];
				long total = root.getTotalSpace();
				if (total <= 0L) {
					continue;
				}
				long usable = root.getUsableSpace();
				long used = total - usable;
				DiskInfoVo disk = new DiskInfoVo();
				disk.setDevName(root.getAbsolutePath());
				disk.setDirName(root.getAbsolutePath());
				disk.setTotalSize(total/MB);
				disk.setAvailSize(usable/MB);
				disk.setUsedSize(used/MB);
				disk.setUsePercent(String.format(Locale.US, "%.2f%%", used * 100D / total));
				disk.setTypeName("local");
				disk.setSysTypeName(System.getProperty("os.name"));

				String val = diskWritesAndReadsOnInit.get(root.getAbsolutePath());
				if(val != null){
					long timePeriod = Math.max(1L, (System.currentTimeMillis() - initTime)/1000);
					long origRead = Long.parseLong(val.split("\\|")[0]);
					long origWrite = Long.parseLong(val.split("\\|")[1]);
					disk.setDiskReadRate((double)(0L - origRead)/timePeriod);
					disk.setDiskWriteRate((double)(0L - origWrite)/timePeriod);
				}

				status.getDiskInfos().add(disk);
			}
		} catch (Exception e) {

		}
	}
    
    public static void getNetInfo(ServerStatusVo status){
    	// JDK does not expose cross-platform network traffic counters.
    }
    
    public static void main(String[] args) {
		try {
			ServerStatusVo status = getServerAllStatus();
			CpuInfoVo x = status.getCpuInfos().get(0);
			System.out.println(x.getIdlePercent());
			System.out.println(x.getUsedPercent());
		} catch (Exception e) {
			// TODO: handle exception
		} 

	}
}
