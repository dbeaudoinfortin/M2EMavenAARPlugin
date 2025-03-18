package com.dbf.m2e.aarunpack;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.maven.plugin.MojoExecution;
import org.apache.maven.project.MavenProject;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.m2e.core.embedder.IMavenExecutionContext;
import org.eclipse.m2e.core.project.IMavenProjectFacade;
import org.eclipse.m2e.core.project.configurator.AbstractBuildParticipant;

public class AARUnpackBuildParticipant extends AbstractBuildParticipant {

	private static final String CONTEXT_CLASSPATH = "aar-unpack-classpath";
	private static final String CONTEXT_EXTRACT_DIR = "aar-unpack-extract-dir";
	
	@Override
	public Set<IProject> build(int kind, IProgressMonitor monitor) throws CoreException {
		
		IMavenProjectFacade projectFacade = getMavenProjectFacade();
        if (projectFacade == null) {
            return Collections.emptySet();
        }

        System.out.println("AAR Unpack Plugin: Running build participant...");
        try {
        	 List<MojoExecution> executions = projectFacade.getMojoExecutions("io.github.dbeaudoinfortin", "maven-aar-unpack-plugin", monitor, "aar-unpack");
             for(MojoExecution execution : executions) {
            	 System.out.println("AAR Unpack Plugin: Running Mojo Execution " + execution.toString());
            	
            	 //Run the Maven goal
                IMavenExecutionContext context = projectFacade.createExecutionContext();
                context.execute(projectFacade.getMavenProject(), execution, monitor);
             }
        } catch (Exception e) {
            throw new CoreException(org.eclipse.core.runtime.Status.error("Failed to execute AAR Unpack plugin in Eclipse build", e));
        }

        //Update the Eclipse classpath
        IProject project = projectFacade.getProject();
        
        boolean projectWasModified = false;
        if (project.hasNature(JavaCore.NATURE_ID)) {
            IJavaProject javaProject = JavaCore.create(project);

            if (javaProject.exists()) {
            	projectWasModified = updateClasspath(projectFacade.getMavenProject(), javaProject, monitor);
            }
        }
        
        System.out.println("AAR Unpack Plugin: Execution complete.");
        if(projectWasModified) {
        	Set<IProject> set = new HashSet<IProject>();
        	set.add(project);
        	return set;
        }
        
        return null;
    }
	
	private boolean updateClasspath(MavenProject mavenProject, IJavaProject javaProject, IProgressMonitor monitor) throws JavaModelException {
		boolean modified = false;

		Object extractDirO = mavenProject.getContextValue(CONTEXT_EXTRACT_DIR);
        Path extractDir = (null != extractDirO) ? IPath.fromOSString(extractDirO.toString()).toPath() : null;
        
        List<IClasspathEntry> classpathEntries = new ArrayList<>();
        for (IClasspathEntry entry : javaProject.getRawClasspath()) {
        	if(entry.getEntryKind() == 1 && entry.getContentKind() == 2 
        			&& extractDir != null && entry.getPath() != null 
        			&& entry.getPath().toPath().startsWith(extractDir)) {
        		//This is a classpath entry previously added by this plugin.
        		//Remove it.
        		System.out.println("AAR Unpack Plugin: Removing old classpath entry " + entry.getPath());
        		modified = true;
        	} else {
        		classpathEntries.add(entry);
        	}    
        }
        
        Object classPathO = mavenProject.getContextValue(CONTEXT_CLASSPATH);
        if(null != classPathO || (classPathO instanceof List<?>)) {
        	List<?> classPathList = (List<?>) classPathO;
        	//Add a new library classpath entry to the Eclipse project for each AAR package
        	for(Object newEntryO : classPathList) {
        		if(null != newEntryO || (newEntryO instanceof Map.Entry<?, ?>)) {
        			Map.Entry<?, ?> newEntry = (Map.Entry<?, ?>) newEntryO;
        			if(null == newEntry.getKey()) continue;
        			
        			final String classesPathString = newEntry.getKey().toString();
        			final String sourcePathString = (newEntry.getValue() == null) ? null : newEntry.getValue().toString();
            		final IPath classesPath = IPath.fromOSString(classesPathString);
            		final IPath sourcesPath = (sourcePathString == null) ? null : IPath.fromOSString(sourcePathString);
            		
            		System.out.println("AAR Unpack Plugin: Adding new classpath entry " + classesPathString);
    				IClasspathEntry aarPackage = JavaCore.newLibraryEntry(classesPath, sourcesPath, null);
    				classpathEntries.add(aarPackage);
    				modified = true;
        		}
        		
        	}           
        }
        
		if (modified) {
			javaProject.setRawClasspath(classpathEntries.toArray(new IClasspathEntry[0]), monitor);
			System.out.println("AAR Unpack Plugin: Eclipse project classpath update with extracted AAR dependencies.");
		}

		return modified;
	}
}