package main.java.edu.usb.argos.ASTProcessor.FileReaderByText;

public interface IFileAnalyzer<T,K> {
    K readFile(T code) throws Exception;
}
