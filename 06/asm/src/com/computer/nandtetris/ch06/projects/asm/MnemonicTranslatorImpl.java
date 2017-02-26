package com.computer.nandtetris.ch06.projects.asm;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Set;
import java.util.TreeSet;

public class MnemonicTranslatorImpl implements MnemonicTranslator {

    private static final String UNSPECIFIED_DESTINATION_SPEC = "";
    private static final String UNSPECIFIED_COMPUTE_SPEC = "";
    private static final String UNSPECIFIED_JUMP_SPEC = "";
    private static final int WORD_SIZE = 16;

    private final SymbolTable symbolTable;
    private BufferedWriter writer;

    ImmutableMap<String, String> destinationBitsBySpec;
    ImmutableMap<String, String> computeBitsBySpec;
    ImmutableMap<String, String> jumpBitsBySpec;

    MnemonicTranslatorImpl(SymbolTable symbolTable, BufferedWriter writer) {
        this.symbolTable = symbolTable;
        this.writer = writer;

        destinationBitsBySpec = buildDestinationBitsBySpec();
        computeBitsBySpec = buildComputeBitsBySpec();
        jumpBitsBySpec = buildJumpBitsBySpec();
    }

    private ImmutableMap<String, String> buildDestinationBitsBySpec() {
        return ImmutableMap.<String, String>builder()
                       .put(UNSPECIFIED_DESTINATION_SPEC, "000")
                       .put("M", "001")
                       .put("D", "010")
                       .put("DM", "011")
                       .put("A", "100")
                       .put("AM", "101")
                       .put("AD", "110")
                       .put("ADM", "111")
                       .build();
    }

    private ImmutableMap<String, String> buildJumpBitsBySpec() {
        return ImmutableMap.<String, String>builder()
                       .put(UNSPECIFIED_JUMP_SPEC, "000")
                       .put("JGT", "001")
                       .put("JEQ", "010")
                       .put("JGE", "011")
                       .put("JLT", "100")
                       .put("JNE", "101")
                       .put("JLE", "110")
                       .put("JMP", "111")
                       .build();
    }

    private ImmutableMap<String, String> buildComputeBitsBySpec() {
        ImmutableMap<String, String> templateBitsBySpec =
                ImmutableMap.<String, String>builder()
                        .put("%s", "%s110000")
                        .put("!%s", "%s110001")
                        .put("-%s", "%s110011")
                        .put("%s+1", "%s110111")
                        .put("%s-1", "%s110010")
                        .put("D+%s", "%s000010")
                        .put("D-%s", "%s010011")
                        .put("%s-D", "%s000111")
                        .put("D&%s", "%s000000")
                        .put("D|%s", "%s010101")
                        .build();

        ImmutableMap.Builder builder = ImmutableMap.<String, String>builder();
        builder
                .put(UNSPECIFIED_COMPUTE_SPEC, "0000000")
                .put("0", "0101010")
                .put("1", "0111111")  // bug #2: had copy-pasted the bit-sequence for "0" here.
                .put("-1", "0111010")
                .put("D", "0001100")
                .put("!D", "0001101")
                .put("-D", "0001111")
                .put("D+1", "0011111")
                .put("D-1", "0001110");

        templateBitsBySpec.entrySet()
                .stream()
                .forEach(e -> builder.put(
                                instantiate(e.getKey(), "A"),
                                instantiate(e.getValue(), "0")));
        templateBitsBySpec.entrySet()
                .stream()
                .forEach(e -> builder.put(
                        instantiate(e.getKey(), "M"),
                        instantiate(e.getValue(), "1")));

        return builder.build();
    }

    private String instantiate(String format, String arg) {
        return String.format(format, arg);
    }

    @Override
    public void translate(ParsedLine parsedLine) throws IOException {
        if (parsedLine.getType().equals(ParsedComponents.LineType.PSEUDO_COMMAND)) {
            return;
        }

        Instruction instruction = null;
        if (parsedLine.getType().equals(ParsedComponents.LineType.A_INSTRUCTION)) {
            instruction = createAddressInstruction(parsedLine);
        } else {
            Preconditions.checkArgument(
                    parsedLine.getType().equals(ParsedComponents.LineType.C_INSTRUCTION),
                    parsedLine.toString());
            instruction = createComputeInstruction(parsedLine);
        }

        writer.write(instruction.toString() + "\n");
    }

    Instruction createAddressInstruction(ParsedLine parsedLine) {
        Instruction instruction = new Instruction();
        instruction.append("0");
        Integer address = extractAddress(parsedLine);
        instruction.append(convertToBits(address, WORD_SIZE - 1));
        return instruction;
    }

    private String convertToBits(Integer address, int numBits) {
        return String.format("%" + numBits + "s", Integer.toBinaryString(address))
                       .replace(' ', '0');
    }

    Instruction createComputeInstruction(ParsedLine parsedLine) {
        Instruction instruction = new Instruction();
        instruction.append("1");
        instruction.append("11");
        instruction.append(getComputeBits(parsedLine.getComputeSpec()));
        instruction.append(getDestinationBits(parsedLine.getDestination()));
        instruction.append(getJumpBits(parsedLine.getJumpSpec()));
        return instruction;
    }

    String getDestinationBits(Optional<String> destination) {
        String spec = UNSPECIFIED_DESTINATION_SPEC;
        if (destination.isPresent()) {
            Set<String> destChars = new TreeSet<String>();
            char[] dest = destination.get().toCharArray();
            for (int i = 0; i < dest.length; i++) {
                destChars.add(String.valueOf(dest[i]));
            }
            StringBuilder builder = new StringBuilder();
            destChars.stream().forEach(s -> builder.append(s));
            spec = builder.toString();
        }
        return lookup(destinationBitsBySpec, spec);
    }

    String getComputeBits(Optional<String> computeSpec) {
        String spec = computeSpec.isPresent() ? computeSpec.get() : UNSPECIFIED_COMPUTE_SPEC;
        return lookup(computeBitsBySpec, spec);
    }

    String getJumpBits(Optional<String> jumpSpec) {
        String spec = jumpSpec.isPresent() ? jumpSpec.get() : UNSPECIFIED_JUMP_SPEC;
        return lookup(jumpBitsBySpec, spec);
    }

    private String lookup(ImmutableMap<String, String> bitsBySpec, String spec) {
        String bits = bitsBySpec.get(spec);
        Preconditions.checkNotNull(bits);
        return bits;
    }

    Integer extractAddress(ParsedLine parsedLine) {
        if (parsedLine.getAddress().isPresent()) {
            return parsedLine.getAddress().get();
        }

        Preconditions.checkArgument(parsedLine.getSymbol().isPresent());
        return symbolTable.get(parsedLine.getSymbol().get());
    }
}
