package org.openkoala.organisation.facade.impl;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.openkoala.organisation.SnIsExistException;
import org.openkoala.organisation.application.BaseApplication;
import org.openkoala.organisation.domain.Job;
import org.openkoala.organisation.facade.dto.JobDTO;
import org.openkoala.organisation.facade.impl.assembler.JobDtoAssembler;

/**
 * JobController单元测试
 * @author xmfang
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class JobFacadeImplTest {
	
	@Mock
	private BaseApplication baseApplication;

	@InjectMocks
	private JobFacadeImpl jobFacadeImpl = new JobFacadeImpl();
	
	@Test
	public void testQueryAllJobs() {
		List<Job> jobs = generateJobs();
		when(baseApplication.findAll(Job.class)).thenReturn(jobs);
		assertEquals(assemJobDtos(jobs), jobFacadeImpl.findAllJobs());
	}
	
	private List<Job> generateJobs() {
		List<Job> jobs = new ArrayList<Job>();
		Job job1 = new Job("总公司总经理", "JOB-XXXXX1");
		Job job2 = new Job("总公司副总经理", "JOB-XXXXX2");
		jobs.add(job1);
		jobs.add(job2);
		return jobs;
	}
	
	private List<JobDTO> assemJobDtos(List<Job> jobs) {
		List<JobDTO> results = new ArrayList<JobDTO>();
		for (Job job : jobs) {
			results.add(JobDtoAssembler.assemDto(job));
		}
		return results;
	}
	
	@Test
	public void testCreateJob() {
		JobDTO jobDTO = new JobDTO(2L, "JOB-XXXXX1");
		jobDTO.setName("总公司总经理");
		jobFacadeImpl.createJob(jobDTO);
		verify(baseApplication, only()).saveParty(JobDtoAssembler.assemEntity(jobDTO));
	}
	
	@Test
	public void testCatchSnIsExistExceptionWhenCreateJob() {
		JobDTO jobDTO = new JobDTO(2L, "JOB-XXXXX1");
		jobDTO.setName("总公司总经理");
		doThrow(new SnIsExistException()).when(baseApplication).saveParty(JobDtoAssembler.assemEntity(jobDTO));
		assertEquals("职务编码: " + jobDTO.getSn() + " 已被使用！", jobFacadeImpl.createJob(jobDTO).getMessage());
	}
	
	@Test
	public void testExceptionWhenCreateJob() {
		JobDTO jobDTO = new JobDTO(2L, "JOB-XXXXX1");
		jobDTO.setName("总公司总经理");
		doThrow(new RuntimeException()).when(baseApplication).saveParty(JobDtoAssembler.assemEntity(jobDTO));
		assertEquals("保存失败！", jobFacadeImpl.createJob(jobDTO).getMessage());
	}
	
	@Test
	public void testUpdateJob() {
		JobDTO jobDTO = new JobDTO(2L, "JOB-XXXXX1");
		jobDTO.setName("总公司总经理");
		jobFacadeImpl.updateJobInfo(jobDTO);
		verify(baseApplication, only()).updateParty(JobDtoAssembler.assemEntity(jobDTO));
	}
	
	@Test
	public void testCatchSnIsExistExceptionWhenUpdateJob() {
		JobDTO jobDTO = new JobDTO(2L, "JOB-XXXXX1");
		jobDTO.setName("总公司总经理");
		doThrow(new SnIsExistException()).when(baseApplication).updateParty(JobDtoAssembler.assemEntity(jobDTO));
		assertEquals("职务编码: " + jobDTO.getSn() + " 已被使用！", jobFacadeImpl.updateJobInfo(jobDTO).getMessage());
	}
	
	@Test
	public void testExceptionWhenUpdateJob() {
		JobDTO jobDTO = new JobDTO(2L, "JOB-XXXXX1");
		jobDTO.setName("总公司总经理");
		doThrow(new RuntimeException()).when(baseApplication).updateParty(JobDtoAssembler.assemEntity(jobDTO));
		assertEquals("修改失败！", jobFacadeImpl.updateJobInfo(jobDTO).getMessage());
	}
	
	@Test
	public void testGet() {
		Job job = new Job("总公司总经理", "JOB-XXXXX1");
		Long jobId = 6L;
		job.setId(jobId);
		
		when(baseApplication.getEntity(Job.class, jobId)).thenReturn(job);
		assertEquals(JobDtoAssembler.assemDto(job), jobFacadeImpl.getJobById(jobId));
	}
	
	@Test
	public void testTerminateEmployee() {
		JobDTO jobDTO = new JobDTO(2L, "JOB-XXXXX1");
		jobDTO.setName("总公司总经理");
		jobFacadeImpl.terminateJob(jobDTO);
		verify(baseApplication, only()).terminateParty(JobDtoAssembler.assemEntity(jobDTO));
	}
	
}