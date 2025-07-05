package better.anticheat.core.player.tracker.impl.confirmation;

import better.anticheat.core.util.type.fastlist.FastObjectArrayList;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Objects;

/**
 * Represents the state of a transaction confirmation.
 * This is used to track confirmations sent to the client, which must be acknowledged.
 * Supports both long IDs (for keepalive/ping) and byte array IDs (for cookies).
 */
@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class ConfirmationState {

    private final Object id; // Can be Long or byte[]
    private final ConfirmationType type;
    private final long timestamp;
    private final boolean needsCancellation;
    private final FastObjectArrayList<Runnable> listeners = new FastObjectArrayList<>();

    private long timestampConfirmed = -1L;

    /**
     * Constructor for long-based IDs (keepalive, ping)
     *
     * @param id The ID of the confirmation.
     * @param type The type of the confirmation.
     * @param timestamp The time the confirmation was sent.
     * @param needsCancellation Whether the confirmation needs to be cancelled.
     */
    public ConfirmationState(final long id, final ConfirmationType type, final long timestamp, final boolean needsCancellation) {
        this.id = id;
        this.type = type;
        this.timestamp = timestamp;
        this.needsCancellation = needsCancellation;
    }

    /**
     * Constructor for byte array-based IDs (cookies)
     *
     * @param id The ID of the confirmation.
     * @param type The type of the confirmation.
     * @param timestamp The time the confirmation was sent.
     * @param needsCancellation Whether the confirmation needs to be cancelled.
     */
    public ConfirmationState(final byte[] id, final ConfirmationType type, final long timestamp, final boolean needsCancellation) {
        this.id = id.clone(); // Defensive copy
        this.type = type;
        this.timestamp = timestamp;
        this.needsCancellation = needsCancellation;
    }

    /**
     * Gets the ID as a long. Only valid for long-based confirmations.
     *
     * @return the long ID
     * @throws IllegalStateException if the ID is not a long
     */
    public long getLongId() {
        if (!(id instanceof Long)) {
            throw new IllegalStateException("ID is not a long, it is: " + (id != null ? id.getClass().getSimpleName() : "null"));
        }
        return (Long) id;
    }

    /**
     * Gets the ID as a byte array. Only valid for byte array-based confirmations.
     *
     * @return a copy of the byte array ID
     * @throws IllegalStateException if the ID is not a byte array
     */
    public byte[] getByteArrayId() {
        if (!(id instanceof byte[])) {
            throw new IllegalStateException("ID is not a byte array, it is: " + (id != null ? id.getClass().getSimpleName() : "null"));
        }
        return ((byte[]) id).clone(); // Defensive copy
    }

    /**
     * Checks if this confirmation uses a long ID.
     *
     * @return true if the ID is a long
     */
    public boolean hasLongId() {
        return id instanceof Long;
    }

    /**
     * Checks if this confirmation uses a byte array ID.
     *
     * @return true if the ID is a byte array
     */
    public boolean hasByteArrayId() {
        return id instanceof byte[];
    }

    /**
     * Equals should only compare the id and type (the literal confirmation data)
     *
     * @param o the reference object with which to compare.
     * @return true if this object is the same as the obj argument; false otherwise.
     */
    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final var that = (ConfirmationState) o;

        // Handle different ID types
        final boolean idsEqual;
        if (id instanceof byte[] && that.id instanceof byte[]) {
            idsEqual = Arrays.equals((byte[]) id, (byte[]) that.id);
        } else {
            idsEqual = Objects.equals(id, that.id);
        }

        return idsEqual && type == that.type;
    }

    /**
     * HashCode should only compare the id and type (the literal confirmation data)
     *
     * @return the object hash
     */
    @Override
    public int hashCode() {
        if (id instanceof byte[]) {
            return Objects.hash(Arrays.hashCode((byte[]) id), type);
        } else {
            return Objects.hash(id, type);
        }
    }
}
