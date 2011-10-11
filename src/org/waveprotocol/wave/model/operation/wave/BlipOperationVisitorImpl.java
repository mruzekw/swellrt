/**
 * Copyright 2008 Google Inc.
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

package org.waveprotocol.wave.model.operation.wave;

/**
 * Vacuous implementation of a {@code BlipOperationVisitor}, such that visitor implementations only
 * need to implement methods meaningful to their context.
 *
 * TODO(user): this is by convention called "XyzAdapter" in the Java SDK,
 * see for example {@link java.awt.event.WindowAdapter}, remember Impl-suffix
 * is a cop out from finding a real name ;-)
 *
 */
public class BlipOperationVisitorImpl implements BlipOperationVisitor {

  @Override
  public void visitBlipContentOperation(BlipContentOperation op) {}

  @Override
  public void visitSubmitBlip(SubmitBlip op) {}
}
