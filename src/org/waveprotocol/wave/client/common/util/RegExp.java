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

package org.waveprotocol.wave.client.common.util;

import com.google.gwt.core.client.GWT;

import java.util.List;

/**
 * Interface representing a regular expression. GWT does not support the
 * java.util.regex library and the JS implementation does not have the same
 * support. Using this to have Java/GWT regex support agree on a similar
 * interface for both runtime and testing implementations.
 *
 */
public interface RegExp {

  interface Factory {
    RegExp create(String regex);
  }

  Factory FACTORY = GWT.create(RegExp.Factory.class);

  /**
   * Tests if the given string matches the regexp, and returns true if matching,
   *    false if not.
   *
   * @param test the string to test against.
   * @return true if match was found, otherwise false.
   */
  public boolean test(String test);

  /**
   * Searches a string for a specified value. Returns all strings that matched.
   *
   * @param test the string to test against.
   * @return list of matches or empty list is non-found.
   */
  public List<String> getMatches(String test);
}
