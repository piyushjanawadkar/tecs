package com.computer.nandtetris.ch06.projects.asm;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.base.Verify;
import com.google.common.collect.ImmutableList;

import java.io.*;
import java.util.HashMap;
import java.util.LinkedList;

class SymbolTableImpl implements SymbolTable {

    private static final ImmutableList<String> SYMBOLS =
            ImmutableList.of("SP", "LCL", "ARG", "THIS", "THAT");

    private static final int NUM_PREDEFINED_MEMORY_LOCATIONS = 16;

    private static final String SCREEN_LABEL = "SCREEN";
    private static final int SCREEN_BASE_ADDRESS = 0x4000;

    private static final String KEYBOARD_LABEL = "KBD";
    private static final int KEYBOARD_BASE_ADDRESS = 0x6000;

    private HashMap<String, Integer> symbolTable = new HashMap<String, Integer>();
    private LinkedList<String> potentialVariables = new LinkedList<String>();

    private int memoryIndex = 0;

    private final LineParser lineParser;

    public SymbolTableImpl(LineParser lineParser) {
        this.lineParser = lineParser;
        loadPredefinedSymbols();
    }

    private void loadPredefinedSymbols() {
        addSymbolAt(SCREEN_LABEL, SCREEN_BASE_ADDRESS);
        addSymbolAt(KEYBOARD_LABEL, KEYBOARD_BASE_ADDRESS);

        memoryIndex = 0;
        for (String symbol : SYMBOLS) {
            addSymbol(symbol);
        }

        memoryIndex = 0;
        for (int i = 0; i < NUM_PREDEFINED_MEMORY_LOCATIONS; i++) {
            addSymbol("R" + i);
        }
    }

    private void addSymbolAt(String label, int baseAddress) {
        memoryIndex = baseAddress;
        addSymbol(label);
    }

    private void addSymbol(String symbol) {
        if (!symbolTable.containsKey(symbol)) {
            //System.out.println(symbol + " @" + memoryIndex);
            symbolTable.put(symbol, memoryIndex);
            memoryIndex++;
        }
    }

    @Override
    public void addSymbols() throws IOException {
        LinkedList<String> unassignedLabels = new LinkedList<String>();
        ParsedLine parsedLine;
        while ((parsedLine = lineParser.getNextParsedLine()) != null) {
            collectSymbols(parsedLine, unassignedLabels);
        }
        Verify.verify(unassignedLabels.isEmpty());

        updateSymbolTable();
    }

    @Override
    public Integer get(String symbol) {
        Integer address = symbolTable.get(symbol);
        Preconditions.checkNotNull(address);
        return address;
    }

    private void updateSymbolTable() {
        for (String potentialVariable : potentialVariables) {
            addSymbol(potentialVariable);
        }
    }

    private void collectSymbols(
            ParsedLine parsedLine, LinkedList<String> unassignedLabels) {
        Optional<String> parsedSymbol = parsedLine.getSymbol();
        if (parsedLine.getType().equals(ParsedComponents.LineType.PSEUDO_COMMAND)) {
            unassignedLabels.add(parsedSymbol.get());
            return;
        }

        assignAddress(parsedLine.getIndex(), unassignedLabels);
        if (parsedLine.getType().equals(ParsedComponents.LineType.A_INSTRUCTION)) {
            if (parsedSymbol.isPresent()) {
                potentialVariables.add(parsedSymbol.get());
            }
            return;
        }

        Verify.verify(
                parsedLine.getType().equals(
                        ParsedComponents.LineType.C_INSTRUCTION), parsedLine.toString());
    }

    private void assignAddress(Integer address, LinkedList<String> unassignedLabels) {
        for (String unassignedLabel : unassignedLabels) {
            Preconditions.checkArgument(!symbolTable.containsKey(unassignedLabel));
            //System.out.println(unassignedLabel + " @" + address);
            symbolTable.put(unassignedLabel, address);
        }
        unassignedLabels.clear();
    }
}
