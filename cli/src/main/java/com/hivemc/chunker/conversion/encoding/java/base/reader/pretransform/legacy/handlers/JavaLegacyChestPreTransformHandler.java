package com.hivemc.chunker.conversion.encoding.java.base.reader.pretransform.legacy.handlers;

import com.hivemc.chunker.conversion.handlers.pretransform.Edge;
import com.hivemc.chunker.conversion.handlers.pretransform.manager.handler.block.BlockPreTransformHandler;
import com.hivemc.chunker.conversion.intermediate.column.ChunkerColumn;
import com.hivemc.chunker.conversion.intermediate.column.chunk.identifier.ChunkerBlockIdentifier;
import com.hivemc.chunker.conversion.intermediate.column.chunk.identifier.type.block.states.vanilla.VanillaBlockStates;
import com.hivemc.chunker.conversion.intermediate.column.chunk.identifier.type.block.states.vanilla.types.ChestType;
import com.hivemc.chunker.conversion.intermediate.column.chunk.identifier.type.block.states.vanilla.types.FacingDirectionHorizontal;

import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Calculates whether a chest is a single chest or if it is a double chest and which side it is.
 */
public class JavaLegacyChestPreTransformHandler implements BlockPreTransformHandler {
    @Override
    public Set<Edge> getRequiredEdges(ChunkerColumn column, int x, int y, int z, ChunkerBlockIdentifier blockIdentifier) {
        // Check adjacent edges
        return calculateEdges(x, y, z, Direction.getAdjacentFaces(blockIdentifier.getState(VanillaBlockStates.FACING_HORIZONTAL)));
    }

    @Override
    public ChunkerBlockIdentifier handle(ChunkerColumn column, Map<Edge, ChunkerColumn> neighbours, int x, int y, int z, ChunkerBlockIdentifier blockIdentifier) {
        FacingDirectionHorizontal facingDirection = Objects.requireNonNull(blockIdentifier.getState(VanillaBlockStates.FACING_HORIZONTAL));

        // Check right
        ChunkerBlockIdentifier right = getRelative(column, neighbours, x, y, z, facingDirection.rotateAntiClockwise().asDirection());
        if (right.getType().equals(blockIdentifier.getType())) {
            return blockIdentifier.copyWith(VanillaBlockStates.CHEST_TYPE, ChestType.RIGHT);
        }

        // Check left
        ChunkerBlockIdentifier left = getRelative(column, neighbours, x, y, z, facingDirection.rotateClockwise().asDirection());
        if (left.getType().equals(blockIdentifier.getType())) {
            return blockIdentifier.copyWith(VanillaBlockStates.CHEST_TYPE, ChestType.LEFT);
        }

        // Otherwise it's a single chest
        return blockIdentifier.copyWith(VanillaBlockStates.CHEST_TYPE, ChestType.SINGLE);
    }
}