package com.computer.nandtetris.ch06.projects.asm;

import com.google.common.base.Preconditions;

import java.nio.file.FileSystems;
import java.nio.file.Path;

class HackFilePath {

    private static final String EXTENSION_ASM = ".asm";
    private static final String EXTENSION_HACK = ".hack";

    private final Path path;
    private final String fileName;

    static HackFilePath from(String inputFilePath) {
        return new HackFilePath(FileSystems.getDefault().getPath(inputFilePath));
    }

    private HackFilePath(Path path) {
        this.path = path;
        this.fileName = path.getFileName().toString();
        Preconditions.checkArgument(fileName.endsWith(EXTENSION_ASM));
    }

    public String get() {
        return path.resolveSibling(
                fileName
                        .replaceFirst(
                                String.format("%s$", EXTENSION_ASM),
                                EXTENSION_HACK)).toString();
    }
}
