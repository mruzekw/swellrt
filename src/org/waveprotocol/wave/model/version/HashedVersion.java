/*
 * Copyright (C) 2009 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.waveprotocol.wave.model.version;

import com.google.common.annotations.VisibleForTesting;

import org.waveprotocol.wave.model.util.CharBase64;
import org.waveprotocol.wave.model.util.Preconditions;

import java.util.Arrays;

/**
 * A version number and a cryptographic hash of the deltas preceding that
 * version.
 *
 * @author anorth@google.com (Alex North)
 */
public final class HashedVersion implements Comparable<HashedVersion> {
  /**
   * Constructs a hashed version with the specified version and historyHash.
   */
  public static HashedVersion of(long version, byte[] historyHash) {
    return new HashedVersion(version, historyHash);
  }

  /**
   * Constructs an un-hashed (i.e. unsigned) version with a version number
   * and empty hash.
   *
   * <p>
   * The value generated by this method is the smallest hashed version
   * with this version number. That is,
   * {@code hv.compareTo(HashedVersion.unsigned(hv.getVersion())) >= 0},
   * for all hashed versions {@code hv}.
   *
   * TODO(soren): this method should be renamed to unhashed().
   */
  public static HashedVersion unsigned(long version) {
    return new HashedVersion(version, new byte[0]);
  }

  private final long version;
  private final byte[] historyHash;

  private HashedVersion(long version, byte[] historyHash) {
    Preconditions.checkArgument(version >= 0, "negative version");
    Preconditions.checkNotNull(historyHash, "null history hash");
    this.version = version;
    this.historyHash = historyHash;
  }

  /**
   * {@inheritDoc}
   *
   * Lexicographic comparison of version and historyHash.
   */
  @Override
  public int compareTo(HashedVersion other) {
    return version != other.version
        ? Long.signum(version - other.version)
        : compare(historyHash, other.historyHash);
  }

  /** The number of ops that led to this version. */
  public long getVersion() {
    return version;
  }

  /** A hash over the history of deltas up to this version. */
  public byte[] getHistoryHash() {
    return historyHash;
  }

  @Override
  public int hashCode() {
    int result = 17;
    result = 31 * result + Long.valueOf(version).hashCode();
    result = 31 * result + Arrays.hashCode(historyHash);
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    } else  if (!(obj instanceof HashedVersion)) {
      return false;
    } else {
      HashedVersion that = (HashedVersion) obj;
      return version == that.version && Arrays.equals(historyHash, that.historyHash);
    }
  }

  @Override
  public String toString() {
    return Long.toString(version) + ":" + CharBase64.encode(historyHash);
  }

  /**
   * Lexicographic comparison of two byte arrays.
   *
   * @return -1, 0, or 1
   */
  private static int compare(byte[] first, byte[] second) {
    if (first == second) {
      return 0; // no need to compare contents
    }
    for (int i = 0; i < first.length && i < second.length; i++) {
      if (first[i] != second[i]) {
        return Integer.signum(first[i] - second[i]);
      }
    }
    // Bytes are equal up to the length of the shortest array. Then longest is bigger.
    return Integer.signum(first.length - second.length);
  }
}
