package com.chrissyx.jfix.modules.util;

import com.chrissyx.jfix.common.error.JFixError;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

/**
 * Utils for file and directory operations.
 *
 * @author Chrissyx
 */
public class FileUtils
{
    /**
     * Comparator to list directories before files.
     */
    private static final Comparator<File> FILE_COMPARATOR = new Comparator<File>()
    {
        /**
         * {@inheritDoc}
         */
        @Override
        public int compare(final File file1, final File file2)
        {
            return (file1.isDirectory() && file2.isDirectory()) || (!file1.isDirectory() && !file2.isDirectory()) ? 0
                    : (file1.isDirectory() && !file2.isDirectory() ? -1 : 1);
        }
    };

    /**
     * Hidden constructor to prevent instances of this class.
     */
    private FileUtils()
    {
    }

    /**
     * Returns listing with all found files from stated package and given file ending of this JAR.
     *
     * @param packageName Name of package to look up
     * @param fileEnding File ending to consider
     * @param exclude Names of files to exclude from search result
     * @return Found files from given package with matching file ending
     */
    public static String[] getPackageListing(final String packageName, final String fileEnding, final String... exclude)
    {
        final LinkedList<String> classList = new LinkedList<String>();
        JarEntry curJarEntry;
        String curName;
        try
        {
            final JarInputStream jarInputStream = new JarInputStream(new FileInputStream("JFix.jar"));
            while((curJarEntry = jarInputStream.getNextJarEntry()) != null)
                if((curName = curJarEntry.getName()).contains(packageName) && curName.endsWith(fileEnding))
                    classList.add(curName.substring(curName.lastIndexOf("/") + 1, curName.lastIndexOf(".")));
        }
        catch(final FileNotFoundException e)
        {
            new JFixError("JFix JAR for package listing not found!", e).error(FileUtils.class);
        }
        catch(final IOException e)
        {
            new JFixError(e).error(FileUtils.class);
        }
        if(exclude != null)
            classList.removeAll(Arrays.asList(exclude));
        return classList.toArray(new String[0]);
    }

    /**
     * Returns a file listing from stated directory. ".." is added to address the super folder.
     *
     * @param directory Directory to list files from
     * @param sort List directories before files
     * @return File listing with super folder notation
     */
    public static String[] getFileListing(final File directory, final boolean sort)
    {
        if(!directory.isDirectory())
            throw new IllegalArgumentException(directory + " is not a directory to list files from!");
        final File[] fileList = directory.listFiles();
        if(sort)
            Arrays.sort(fileList, FileUtils.FILE_COMPARATOR);
        final LinkedList<String> filenameList = new LinkedList<String>();
        if(directory.getParentFile() != null)
            filenameList.add("..");
        for(final File curFile : fileList)
            filenameList.add(curFile.getName());
        return filenameList.toArray(new String[0]);
    }
}
