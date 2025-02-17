/*
 * Copyright 2013-2022 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.data.rest.tests.geode;

import org.springframework.data.rest.tests.CommonWebTests;
import org.springframework.hateoas.LinkRelation;
import org.springframework.test.context.ContextConfiguration;

/**
 * @author Oliver Gierke
 */
@ContextConfiguration(classes = GeodeRepositoryConfig.class)
class GeodeWebTests extends CommonWebTests {

	/*
	 * (non-Javadoc)
	 * @see org.springframework.data.rest.webmvc.AbstractWebIntegrationTests#expectedRootLinkRels()
	 */
	@Override
	protected Iterable<LinkRelation> expectedRootLinkRels() {
		return LinkRelation.manyOf("products");
	}
}
