/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.mavenproject10;

/**
 *
 * @author mashalbutt
 */
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
class Multithreading
{
    private final ConcurrentHashMap<String, Integer> wordCountMap;
    public Multithreading()
    {
        this.wordCountMap = new ConcurrentHashMap<>();
    }
    public void countWords(String[] words)
    {
        for (String word : words) 
        {
            wordCountMap.merge(word, 1, Integer::sum);  // Update word count using ConcurrentHashMap's atomic methods
        }
    }

    public void displayWordCount() 
    {
        wordCountMap.forEach((word, count) -> System.out.println(word + ": " + count));
    }
}

class FileProcessor implements Runnable
{
    private final String filePath;
    private final Multithreading wordCounter;

    public FileProcessor(String filePath, Multithreading wordCounter)
    {
        this.filePath = filePath;
        this.wordCounter = wordCounter;
    }
    @Override
    public void run() 
    {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) 
        {
            String line;
            while ((line = reader.readLine()) != null)
            {
                String[] words = line.split("\\s+");
                wordCounter.countWords(words);
            }
        } 
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}

public class MultithreadingCount
{
    public static void main(String[] args) 
    {
        String filePath = "sample.txt";
        int numThreads = 4;
        Multithreading wordCounter = new Multithreading(); //instance of multithreading
        Thread[] threads = new Thread[numThreads]; //multiple instances of file thread
        for (int i = 0; i < numThreads; i++)
        {
            threads[i] = new Thread(new FileProcessor(filePath, wordCounter));
        }
        for (Thread thread : threads)   //concurrently start threads
        {
            thread.start();
        }
        // Wait for all threads to finish
        for (Thread thread : threads)
        {
            try 
            {
                thread.join();
            } 
            catch (InterruptedException e) 
            {
                e.printStackTrace();
            }
        }
        wordCounter.displayWordCount();
    }
}

