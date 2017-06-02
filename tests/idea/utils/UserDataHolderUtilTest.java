package net.rentalhost.idea.utils;

import com.intellij.openapi.util.Key;
import com.intellij.psi.PsiFile;
import org.junit.Assert;

import java.util.List;

import net.rentalhost.suite.FixtureSuite;

public class UserDataHolderUtilTest extends FixtureSuite {
    public void testFrom() {
        final PsiFile           sampleFile = getResourceFile("utils/DummyFile.php");
        final Key<List<String>> sampleKey  = new Key("SampleKey");

        final Object instanceOriginal    = UserDataHolderUtil.from(sampleFile, sampleKey, Object::new);
        final Object instanceShoudBeSame = UserDataHolderUtil.from(sampleFile, sampleKey, Object::new);

        Assert.assertSame(instanceShoudBeSame, instanceOriginal);

        sampleFile.putUserData(sampleKey, null);

        final Object instanceShoudBeAnotherInstance = UserDataHolderUtil.from(sampleFile, sampleKey, Object::new);

        Assert.assertNotSame(instanceShoudBeAnotherInstance, instanceOriginal);
    }
}
