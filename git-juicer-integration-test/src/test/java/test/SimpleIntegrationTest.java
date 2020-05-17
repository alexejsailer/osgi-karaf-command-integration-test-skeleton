/*
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
 */
package test;


import com.dualexec.gitjuicer.core.api.GitStateService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ops4j.pax.exam.Configuration;
import org.ops4j.pax.exam.ConfigurationManager;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.options.MavenArtifactUrlReference;
import org.ops4j.pax.exam.options.MavenUrlReference;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

import java.io.File;

import static org.ops4j.pax.exam.CoreOptions.*;
import static org.ops4j.pax.exam.karaf.options.KarafDistributionOption.*;

@RunWith(PaxExam.class)
@ExamReactorStrategy(PerClass.class)
public class SimpleIntegrationTest {

    private static Logger LOG = LoggerFactory.getLogger(SimpleIntegrationTest.class);

    @Inject
    protected GitStateService gitStateService;

    @Configuration
    public Option[] config() {
        MavenArtifactUrlReference karafUrl = maven()
                .groupId("org.apache.karaf")
                .artifactId("apache-karaf")
                .version(karafVersion())
                .type("tar.gz");

        MavenUrlReference karafStandardRepo = maven()
                .groupId("org.apache.karaf.features")
                .artifactId("standard")
                .version(karafVersion())
                .classifier("features")
                .type("xml");
        return new Option[]{
                // KarafDistributionOption.debugConfiguration("5005", true),
                karafDistributionConfiguration()
                        .frameworkUrl(karafUrl)
                        .unpackDirectory(new File("target", "exam"))
                        .useDeployFolder(false),
                keepRuntimeFolder(),
                configureConsole().ignoreLocalConsole(),
                features(karafStandardRepo, "scr"),
                mavenBundle()
                        .groupId("com.dualexec.gitjuicer")
                        .artifactId("git-juicer-core")
                        .versionAsInProject().start(),
                mavenBundle()
                        .groupId("com.dualexec.gitjuicer")
                        .artifactId("git-juicer-core-api")
                        .versionAsInProject().start(),
                junitBundles()
        };
    }

    public static String karafVersion() {
        ConfigurationManager cm = new ConfigurationManager();
        String karafVersion = cm.getProperty("pax.exam.karaf.version", "4.2.8");
        return karafVersion;
    }


    @Test
    public void testAdd() {
        String result = gitStateService.listRepositories().get(0);
        LOG.info("Result of add was {}", result);
        Assert.assertEquals("Hello World", result);
    }

    @Test
    public void testAdd1() {
        String result = gitStateService.listRepositories().get(0);
        LOG.info("Result of add was {}", result);
        Assert.assertEquals("Hello World", result);
    }


}
