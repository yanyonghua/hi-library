package org.devio.hi.library.log;

import androidx.annotation.NonNull;
import androidx.core.util.LogWriter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @Author yanyonghua
 * @Date 2022/10/14-14:04
 * @Des $.
 */
public class HiFilePrinter implements HiLogPrinter {
    private static final ExecutorService EXECUTOR = Executors.newSingleThreadExecutor();
    private final String logPath;
    private final long retentionTime;
    private LogWriter writer;
    private volatile PrintWorker worker;
    public static HiFilePrinter intsance;

    public static synchronized HiFilePrinter getInstance(String logPath, long retentionTime) {
        if (intsance == null) {
            intsance = new HiFilePrinter(logPath, retentionTime);
        }
        return intsance;
    }

    @Override
    public void print(@NonNull HiLogConfig config, int level, String tag, @NonNull String printString) {

    }

    private HiFilePrinter(String logPath, long retentionTime) {
        this.logPath = logPath;
        this.retentionTime = retentionTime;
    }

    private class LogWriter {
        private String preFileName;
        private File logFile;
        private BufferedWriter bufferedWriter;

        boolean isReady(){
            return bufferedWriter!=null;
        }
        String getPreFileName(){
            return preFileName;
        }

        boolean ready(String newFileName){
            preFileName = newFileName;
            logFile = new File(logPath,newFileName);
            if (!logFile.exists()){
                File parentFile = logFile.getParentFile();
                if (!parentFile.exists()){
                    parentFile.mkdirs();
                }
                try {
                    logFile.createNewFile();
                } catch (IOException e) {
                    logFile=null;
                    preFileName=null;
                    e.printStackTrace();
                    return false;
                }
            }
            try {
                bufferedWriter = new BufferedWriter(new FileWriter(logFile,true));
            } catch (IOException e) {
                e.printStackTrace();
                logFile=null;
                preFileName=null;
                return false;
            }
            return true;
        }

        boolean close(){
            if (bufferedWriter!=null){
                try {
                    bufferedWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    return false;
                }finally {
                    bufferedWriter=null;
                    logFile=null;
                    preFileName=null;
                }
            }
            return true;
        }
        void append(String flattenedLog){
            try {
                bufferedWriter.write(flattenedLog);
                bufferedWriter.newLine();
                bufferedWriter.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private class PrintWorker implements Runnable{

        private BlockingQueue<HiLogMo> logs =new LinkedBlockingQueue<>();
        private volatile boolean running;
        void put(HiLogMo log){
            try {
                logs.put(log);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

         boolean isRunning() {
            synchronized (this){
                return running;
            }
        }
        void start(){
            synchronized (this){
                EXECUTOR.execute(this);
                running =true;
            }
        }

        @Override
        public void run() {
        HiLogMo logMo;
            while (true){
                try {
                    logMo = logs.take();
                    doPrint(logMo);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    synchronized (this){
                        running =false;
                    }
                }
            }
        }
    }

    private void doPrint(HiLogMo logMo) {
        String preFileName = writer.getPreFileName();
        if (preFileName == null){
            String newFileName = genFileName();
            if (writer.isReady()){
                writer.close();
            }
            if (!writer.ready(newFileName)){
                return;
            }
        }
        writer.append(logMo.flattenedLog());
    }

    private String genFileName() {
        SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        sdf.setTimeZone(TimeZone.getDefault());
        return sdf.format(new Date(System.currentTimeMillis()));
    }
}
