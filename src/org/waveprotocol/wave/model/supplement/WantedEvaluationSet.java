/**
 * Copyright 2009 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package org.waveprotocol.wave.model.supplement;

import org.waveprotocol.wave.model.id.WaveletId;

import java.util.Set;

/**
 * The results from multiple agents evaluating the wantedness of a wavelet.
 *
 * This object is immutable.
 *
 */
public interface WantedEvaluationSet {

  /**
   * Gets a view of all of the evaluations in this set. This method is intended
   * to be used only when exploring the deeper reasons behind an overall
   * evaluation. For the most uses, {@link #isWanted()} and {@link
   * #getMostCertain()} ought to provide sufficient information.
   */
  Set<WantedEvaluation> getEvaluations();

  /**
   * Gets the most certain evaluation.
   */
  WantedEvaluation getMostCertain();

  /** Gets the most recent evaluation */
  WantedEvaluation getMostRecent();

  /** Gets the id of the wavelet his set is for. */
  WaveletId getWaveletId();

  /**
   * Whether the wavelet was most recently ignored or not.
   *
   * @return true if the most recent evaluation has ignore set, false otherwise
   */
  boolean isIgnored();

  /**
   * Whether the wavelet is wanted or not.
   *
   * @return true if wavelet is wanted, false if wavelet is not.
   */
  boolean isWanted();
}
