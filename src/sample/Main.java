/*
Code written by Nick Stantzos. Completed on 08/30/2020.
This code will take user input in the form of a directory path, then print out directory and file information for
each child of this root directory. The console will show metadata including: directory name, size (in bytes), file
count. Each node (directory/file) has a list of children that gets populated based on any file contained within the
directory. This Node object is a custom class with some properties that are used for matching children to their parents.

The program works by utilizing the java.io.File package's File.listFiles() built in method to populate an array of
File objects. The array is then iterated through recursively to gather each node, and distinguish directories and files.
After the nodes are collected into a global array list, the list is then sorted through where each child is paired to
their parent. Then, the root node is gathered (the directory input by the user), which by now will contain all of the
children nodes. This is the final tree data structure created before being printed out by the recursive print function.
The print function will add tabs to indicate levels of depth for each file/directory below the root directory.
 */

package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javax.print.attribute.standard.PrinterMessageFromOperator;
import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class Main extends Application
{

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        // Code for JavaFX implementation that I did not have time to complete
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();
    }

    // Global Variables
    // NodeList that will be used frequently
    public static ArrayList<Node> nodeList = new ArrayList<>();
    // NodeID is a unique identifier attached to each node created for later use
    public static int NodeID = 0;

    public static void main(String[] args)
    {
        // Prompt the user for a file path, while giving an example
        System.out.println("Enter a valid directory path, like C:\\Users\\BobD\\Desktop\\Folder");

        // Create new scanner object, initialize to null
        Scanner inputScanner = new Scanner(System.in);
        String inputFilePath = null;

        // Try catch statement to check for error in user input
        try
        {
            // Collect user input, assign to string
            inputFilePath = inputScanner.nextLine();
        }
        catch (Exception e)
        {
            System.out.println("Error in input. Enter a valid directory path.");
        }

        // Provide full path for directory(change accordingly)
        String mainDirectoryPathString = "C:\\Users\\Nick\\Desktop\\TestFolder";

        // File object created from user-specified string
        File mainDirectory = new File(inputFilePath);

        // If the specified directory exists and if it is a directory
        if(mainDirectory.exists() && mainDirectory.isDirectory())
        {
            // Create an array of files that are all children of the mainDirectory
            File[] fileArray = mainDirectory.listFiles();

            // Create a Node object and define it to be the root folder of the specified file structure
            Node<String> root = new Node<>(mainDirectoryPathString);

            // Set root Node fields
            root.ParentName = "root";
            root.NodeName = mainDirectory.getPath();
            root.NodeID = NodeID;
            root.IsRoot = true;
            root.IsFolder = true;
            root.FileSize = mainDirectory.length();
            root.NumberOfFiles = fileArray.length;
            NodeID++;

            // Add the root Node to the nodeList
            nodeList.add(root);

            // Call recursive method to parse the file structure and assign each file/folder to a node
            RecursiveDataDig(fileArray,0,0, root);
        }

        // Loop through the nodeList.
        for (Node<String> i: nodeList)
        {
            // While holding each node at a time, loop through the list again
            for(Node<String> j: nodeList)
            {
                // If the node being held has a name that equals the looped node's parent's name
                if (i.NodeName.equals(j.ParentName))
                {
                    // Add the looped node to the held node's list of children
                    i.addChild(j);

                    // If the added child is a file
                    if (j.IsFile)
                    {
                        // Set the ContainsFiles flag of the parent node to true
                        i.ContainsFiles = true;
                    }
                    // If the added child is not a file
                    else
                    {
                        // Set the ContainsFiles flag of the parent node to false
                        i.ContainsFiles = false;
                    }
                }
            }
        }

        // Grab the first node from the node list. This will be the root node with all children attached
        Node<String> root = nodeList.get(0);

        // Begin printing the file structure, starting with the root directory
        System.out.println("****************Printing retrieved file structure**************");
        //PrintFileName(root);
        RecursivePrint(root,0);

        //launch(args);
    }

    private static void PrintFileName(Node<String> root)
    {
        // Get the last index of the '\' character in the NodeName (the full file path)
        int index = root.NodeName.lastIndexOf("\\");
        // Create a new file name using only the substring after the last occurring '\' character
        String fileName = root.NodeName.substring(index + 1);

        // If the node is a directory, include the characters "->" in the console window
        if (root.IsFolder || root.IsRoot)
        {
            Long byteSize = 0l;
            for (Node i : root.getChildren())
            {
                if (i.IsFolder)
                {
                    // Do nothing, folder byte sizes don't return to any sensible number
                }
                else
                {
                    byteSize += i.FileSize;
                    //byteSize = 1337l;
                }
            }
            System.out.println("->" + fileName + " has " + root.getChildren().size() + " files, with a total byte size of " + byteSize + " bytes");
        }
        // If the node is a file, don't include special characters
        else
        {
            System.out.println(fileName);
        }
    }

    private static void RecursivePrint(Node<String> root, int depth)
    {
        // If the Node passed is a folder
        if (root.IsFolder)
        {
            // Print the current Node and depth
            // For each level of depth, print out one tab
            for (int j = 0; j < depth; j++)
            {
                System.out.print("\t");
            }

            // Print the Node information
            PrintFileName(root);

            // For each child of the current node, call this method recursively
            for (Node<String> k: root.getChildren())
            {
                RecursivePrint(k, depth + 1);
            }

        }
        // For non-directories
        else
        {
            // For each level of depth, print out one tab
            for (int j = 0; j < depth; j++)
            {
                System.out.print("\t");
            }
            PrintFileName(root);
        }
        return;
    }

    static void RecursiveDataDig(File[] fileArray, int index, int level, Node<String> parentNode)
    {
        // If the current index equals the total array length, then exit the recursive function
        if(index == fileArray.length)
        {
            return;
        }

        // If the current element in the fileArray is a file (as opposed to a folder)
        if(fileArray[index].isFile())
        {
            // Create a Node object, assign the file to it
            Node<String> file = new Node<String>(fileArray[index].getName());

            // Set the file's parent Node as the Node provided to the method
            file.setParent(parentNode);

            // Assign Node properties
            file.ParentName = fileArray[index].getParent();
            file.NodeName = fileArray[index].getPath();
            file.NodeID = NodeID;
            file.IsRoot = false;
            file.IsFile = true;
            file.IsFolder = false;
            file.FileSize = fileArray[index].length();
            NodeID++;

            // Add the Node to the nodeList
            nodeList.add(file);
        }
        // If the current element in the fileArray is a folder
        else if(fileArray[index].isDirectory())
        {
            // Create a new Node object as assign the array element to it
            Node<String> folder = new Node<String>(fileArray[index].getName());

            // Set the method-provided parentNode as this Node's parent
            folder.setParent(parentNode);

            // Assign the folder Node's properties
            folder.ParentName = fileArray[index].getParent();
            folder.NodeName = fileArray[index].getPath();
            folder.NodeID = NodeID;
            folder.IsRoot = false;
            folder.IsFile = false;
            folder.IsFolder = true;
            NodeID++;

            // Add the Node to the nodeList
            nodeList.add(folder);

            // Since this Node is a subdirectory, assign this Node as the new parent and make a recursive call to this
            // method to go one level deeper
            parentNode = new Node<String>(fileArray[index].getName());
            RecursiveDataDig(fileArray[index].listFiles(), 0, level + 1, folder);
        }

        // Recursive call for the main directory
        RecursiveDataDig(fileArray,++index, level, parentNode);
    }

}
