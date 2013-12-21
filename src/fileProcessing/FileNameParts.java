package fileProcessing;

import java.io.File;


/**
 * Figures out the directory, extension and core file name (everything except directory and extension)
 * of a file name
 * @author avantis
 */
public class FileNameParts {

	String fileDirectory;
	String coreFileName;
	String extension;
	
	public FileNameParts(File aFile) {
		String filePath = aFile.getAbsolutePath();
		String fileNameMinusPath = filePath.replaceAll("^.*[\\\\|/]", "");
		this.coreFileName = fileNameMinusPath.replaceAll("\\..*$", "");
		this.fileDirectory = filePath.replace(fileNameMinusPath, "");
		this.extension = fileNameMinusPath.replace(coreFileName, "");
	}
	
	
	public String getFileDirectoy() {
		return fileDirectory;
	}
	
	public String getCoreFilename() {
		return coreFileName;
	}
	
	public String getExtension() {
		return extension;
	}

	public String getFilenameMinusExtension() {
		return this.fileDirectory+this.coreFileName;
	}
}
