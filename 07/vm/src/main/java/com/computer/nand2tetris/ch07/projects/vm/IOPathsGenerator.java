package com.computer.nand2tetris.ch07.projects.vm;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;

import java.io.File;
import java.io.FileFilter;
import java.util.Arrays;
import java.util.List;

/**
 * Created by jpiyush on 3/21/17.
 */
class IOPathsGenerator {
    private static final String VM_FILE_EXTENSION = "vm";
    private static final String ASM_FILE_EXTENSION = "asm";

    private static final FileFilter IS_VM_PATH = new FileFilter() {

        public boolean accept(File path) {
            return hasVMSuffix(path.getName());
        }
    };

    private final String inputPath;

    public IOPathsGenerator(String[] args) {
        inputPath = Iterables.getOnlyElement(Arrays.asList(args));
    }

    public IOPaths generate() {
        return IOPaths.create(generateVmFiles(inputPath), generateOutputFile(inputPath));
    }

    private static ImmutableList<String> generateVmFiles(String inputPath) {
        File path = new File(inputPath);
        if (!path.isDirectory()) {
            Preconditions.checkArgument(IS_VM_PATH.accept(path));
            return ImmutableList.of(inputPath);
        }

        File[] vmFiles = path.listFiles(IS_VM_PATH);
        return Arrays.stream(vmFiles).map(File::getAbsolutePath).collect(ImmutableList.toImmutableList());
    }

    private String generateOutputFile(String inputPath) {
        return stripOptionalSuffix(VM_FILE_EXTENSION) + ASM_FILE_EXTENSION;
    }

    private static boolean hasVMSuffix(String path) {
        return path.endsWith("." + VM_FILE_EXTENSION);
    }

    private String stripOptionalSuffix(String inputLocation) {
        String asmExtension = getExtension(ASM_FILE_EXTENSION);
        if (!hasVMSuffix(inputLocation)) {
            return inputLocation + asmExtension;
        }

        return inputLocation.replaceFirst(getExtension(VM_FILE_EXTENSION) + "$", asmExtension);
    }

    private static String getExtension(String extension) {
        return String.format(".%s", extension);
    }
}
