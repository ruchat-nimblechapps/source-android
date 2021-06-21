package com.xdesign.android.common.harness.utils;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Configuration;
import com.activeandroid.query.Select;
import com.xdesign.android.common.harness.models.Building;
import com.xdesign.android.common.harness.models.Desk;
import com.xdesign.android.common.harness.models.Floor;
import com.xdesign.android.common.lib.utils.ModelUtil;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.lang.reflect.Modifier;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public final class ModelUtilTest {

    @Before
    public void before() {
        ActiveAndroid.initialize(new Configuration.Builder(
                getContext())
                .addModelClass(Building.class)
                .addModelClass(Desk.class)
                .addModelClass(Floor.class)
                .create());
    }

    @After
    public void after() {
        ActiveAndroid.dispose();
        
        final Context context = getContext();
        for (final String db : context.databaseList()) {
            context.deleteDatabase(db);
        }
    }

    @Test
    public void deleteAllSafelyWithoutExclusions() {
        // pre
        final Building building = new Building();
        building.save();
        
        final Floor floor = new Floor(building);
        floor.save();
        
        new Desk().save();
        
        // exec
        ModelUtil.deleteAllSafely(false);
        
        // post
        assertEquals(0, new Select().from(Building.class).count());
        assertEquals(0, new Select().from(Floor.class).count());
        assertEquals(0, new Select().from(Desk.class).count());
    }

    @Test
    public void deleteAllSafelyWithExclusions() {
        // pre
        final Building building = new Building();
        building.save();

        final Floor floor = new Floor(building);
        floor.save();

        new Desk().save();

        // exec
        ModelUtil.deleteAllSafely(true);

        // post
        assertEquals(0, new Select().from(Building.class).count());
        assertEquals(0, new Select().from(Floor.class).count());
        assertEquals(1, new Select().from(Desk.class).count());
    }

    @Test
    public void privateCtor() {
        assertEquals(1, ModelUtil.class.getDeclaredConstructors().length);
        assertTrue(Modifier.isPrivate(
                ModelUtil.class.getDeclaredConstructors()[0].getModifiers()));
    }

    private Context getContext() {
        return InstrumentationRegistry.getTargetContext();
    }
}
