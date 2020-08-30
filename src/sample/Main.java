package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javax.print.attribute.standard.PrinterMessageFromOperator;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Main extends Application
{

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();
    }

    public static ArrayList<Node> nodeList = new ArrayList<>();
    public static int NodeID = 0;


    public static void main(String[] args)
    {
        System.out.println("Hello world");
        // This is where code goes that runs before the GUI is launched below

        // Provide full path for directory(change accordingly)
        String maindirpath = "C:\\Users\\Nick\\Desktop\\TestFolder";



        // File object
        File maindir = new File(maindirpath);

        if(maindir.exists() && maindir.isDirectory())
        {
            // array for files and sub-directories
            // of directory pointed by maindir
            File arr[] = maindir.listFiles();
            Node<String> root = new Node<>(maindirpath);
            root.ParentName = "root";
            root.NodeName = maindir.getPath();
            root.NodeID = NodeID;
            root.IsRoot = true;
            root.FileSize = maindir.length();
            root.NumberOfFiles = arr.length;
            NodeID++;
            nodeList.add(root);

            System.out.println("The file size of root is: " + root.FileSize + " bytes (0 bytes is returned for directories)");
            System.out.println("Root contains " + root.NumberOfFiles + " children.");

            for (File i: arr)
            {
                System.out.println("file name is: " + i.getName());
                if (i.getName() == maindirpath)
                {
                    System.out.println("I found the root folder");
                }
            }

            System.out.println("**********************************************");
            System.out.println("Files from main directory : " + maindir);
            System.out.println("**********************************************");

            // Calling recursive method
            RecursiveDataDig(arr,0,0, root);
        }

        for (Node<String> i: nodeList)
        {
            for(Node<String> j: nodeList)
            {
                if (i.NodeName.equals(j.ParentName))
                {
                    i.addChild(j);
                    if (j.IsFile)
                    {
                        i.ContainsFiles = true;
                    }
                    else
                    {
                        i.ContainsFiles = false;
                    }
                }
            }
        }

        Node<String> root = nodeList.get(0);

        System.out.println("****************Entering recursive print**************");
        PrintFileName(root);
        RecursivePrint(root,0);


        int totalNodeCount = nodeList.size();
        int nodeCounter = 0;

        //RecursiveDataTreePrint(root, 0,0, totalNodeCount, nodeCounter);


        //System.out.println(root.getChildren());

        launch(args);
    }

    private static void PrintFileName(Node<String> root) {
        int index = root.NodeName.lastIndexOf("\\");
        String fileName = root.NodeName.substring(index + 1);
        if (root.IsFolder)
        {
            System.out.println("->" + fileName);
        }
        else
        {
            System.out.println(fileName);
        }
    }

    private static void RecursivePrint(Node<String> root, int depth)
    {
        if (root.getChildren().size() > 0)
        {
            for (Node<String> i : root.getChildren())
            {
                if (i.getChildren().size() > 0)
                {
                    int tempDepth = depth;
                    depth++;
                    for (int j = 0; j < depth; j++)
                    {
                        System.out.print("\t");
                    }
                    PrintFileName(i);
                    RecursivePrint(i,depth);
                    depth = tempDepth;
                }
                else if (i.IsFolder)
                {
                    depth++;
                    for (int j = 0; j < depth; j++)
                    {
                        System.out.print("\t");
                    }
                    PrintFileName(i);
                }
                else
                {
                    depth++;
                    for (int j = 0; j < depth; j++)
                    {
                        System.out.print("\t");
                    }
                    PrintFileName(i);
                }
            }
        }
        else
        {

        }
        return;
    }

    static void RecursiveDataDig(File[] arr, int index, int level, Node<String> parentNode)
    {
        // terminate condition
        // If the current array index equals the total array length, then exit the recursive function
        if(index == arr.length)
        {
            return;
        }

        // tabs for internal levels
        for (int i = 0; i < level; i++)
        {
            System.out.print("\t");
        }

        // for files
        if(arr[index].isFile())
        {
            //System.out.println(arr[index].getName() + " | index: " + index + " depth: " + level);
            System.out.println(arr[index].getName());
            Node<String> file = new Node<String>(arr[index].getName());
            file.setParent(parentNode);
            file.ParentName = arr[index].getParent();
            file.NodeName = arr[index].getPath();
            file.NodeID = NodeID;
            file.IsRoot = false;
            file.IsFile = true;
            file.IsFolder = false;
            NodeID++;
            nodeList.add(file);
            //nodeList.add(new Node<String>(arr[index].getName()));
        }

            // for sub-directories
        else if(arr[index].isDirectory())
        {
            String parentNodeName = arr[index].getName();
            System.out.println("[" + parentNodeName + "]");
            //System.out.println("[" + parentNodeName + "]" + " | index: " + index + " depth: " + level);
            //parentNode.addChild(new Node<String>(parentNodeName));
            Node<String> folder = new Node<String>(arr[index].getName());
            folder.setParent(parentNode);
            //folder.ParentName = parentNode.NodeName;
            folder.ParentName = arr[index].getParent();
            folder.NodeName = arr[index].getPath();
            folder.NodeID = NodeID;
            folder.IsRoot = false;
            folder.IsFile = false;
            folder.IsFolder = true;
            NodeID++;
            nodeList.add(folder);
            // TODO: add something to redefine what the parent node is before passing into recursion again
            parentNode = new Node<String>(arr[index].getName());
            // recursion for sub-directories
            RecursiveDataDig(arr[index].listFiles(), 0, level + 1, folder);
        }

        // recursion for main directory
        RecursiveDataDig(arr,++index, level, parentNode);
    }

}
