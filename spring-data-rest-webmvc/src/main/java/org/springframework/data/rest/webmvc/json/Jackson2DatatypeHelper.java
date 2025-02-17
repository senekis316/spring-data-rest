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
package org.springframework.data.rest.webmvc.json;

import org.hibernate.Version;
import org.springframework.util.ClassUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.hibernate4.Hibernate4Module;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import com.fasterxml.jackson.datatype.joda.JodaModule;

/**
 * Helper class to register datatype modules based on their presence in the classpath.
 *
 * @author Jon Brisbin
 * @author Oliver Gierke
 */
public class Jackson2DatatypeHelper {

	private static final boolean IS_HIBERNATE_AVAILABLE = ClassUtils.isPresent("org.hibernate.Version",
			Jackson2DatatypeHelper.class.getClassLoader());
	private static final boolean IS_HIBERNATE4_MODULE_AVAILABLE = ClassUtils.isPresent(
			"com.fasterxml.jackson.datatype.hibernate4.Hibernate4Module", Jackson2DatatypeHelper.class.getClassLoader());
	private static final boolean IS_HIBERNATE5_MODULE_AVAILABLE = ClassUtils.isPresent(
			"com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module", Jackson2DatatypeHelper.class.getClassLoader());

	private static final boolean IS_JODA_MODULE_AVAILABLE = ClassUtils
			.isPresent("com.fasterxml.jackson.datatype.joda.JodaModule", Jackson2DatatypeHelper.class.getClassLoader());

	public static void configureObjectMapper(ObjectMapper mapper) {

		// Hibernate types
		if (IS_HIBERNATE_AVAILABLE && HibernateVersions.isHibernate4() && IS_HIBERNATE4_MODULE_AVAILABLE) {
			new Hibernate4ModuleRegistrar().registerModule(mapper);
		}

		if (IS_HIBERNATE_AVAILABLE && HibernateVersions.isHibernate5() && IS_HIBERNATE5_MODULE_AVAILABLE) {
			new Hibernate5ModuleRegistrar().registerModule(mapper);
		}

		// JODA time
		if (IS_JODA_MODULE_AVAILABLE) {
			JodaModuleRegistrar.registerModule(mapper);
		}
	}

	private static class HibernateVersions {

		public static boolean isHibernate4() {
			return Version.getVersionString().startsWith("4");
		}

		public static boolean isHibernate5() {
			return Version.getVersionString().startsWith("5");
		}
	}

	private static class Hibernate4ModuleRegistrar {

		public void registerModule(ObjectMapper mapper) {

			Hibernate4Module module = new Hibernate4Module();
			module.enable(Hibernate4Module.Feature.FORCE_LAZY_LOADING);

			mapper.registerModule(module);
		}
	}

	private static class Hibernate5ModuleRegistrar {

		public void registerModule(ObjectMapper mapper) {

			Hibernate5Module module = new Hibernate5Module();
			module.enable(Hibernate5Module.Feature.FORCE_LAZY_LOADING);

			mapper.registerModule(module);
		}
	}

	private static class JodaModuleRegistrar {

		public static void registerModule(ObjectMapper mapper) {
			mapper.registerModule(new JodaModule());
			mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
		}
	}
}
