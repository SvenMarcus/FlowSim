package util;

import org.mockito.internal.stubbing.InvocationContainerImpl;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.verification.DefaultRegisteredInvocations;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.InvocationOnMock;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by sven on 25.03.17.
 */
public abstract class MockitoUtil {


    public static void removeInvocation(InvocationOnMock invocationOnMock) {
        List<Invocation> invocations = getInvocationsOnMock(invocationOnMock);
        for (int i = 0; i < invocations.size(); i++) {
            Invocation invocation = invocations.get(i);
            if (mocksAreEqual(invocationOnMock, invocation) && methodsAreEqual(invocationOnMock, invocation))
                if (invocation.getArguments().length == invocationOnMock.getArguments().length)
                    for (int j = 0; j < invocation.getArguments().length; j++)
                        if (invocation.getArguments()[j].equals(invocationOnMock.getArguments()[j])) {
                            invocations.remove(invocation);
                            return;
                        }
        }
    }

    private static boolean methodsAreEqual(InvocationOnMock invocationOnMock, Invocation invocation) {
        return invocation.getMethod().equals(invocationOnMock.getMethod());
    }

    private static boolean mocksAreEqual(InvocationOnMock invocationOnMock, Invocation invocation) {
        return invocation.getMock().equals(invocationOnMock.getMock());
    }

    private static void removeMatchingInvocations(VerificationData data, List<Invocation> invocations) {
        LinkedList invocationsToRemove = new LinkedList();
        Iterator var4 = invocations.iterator();

        Invocation invocation;
        while (var4.hasNext()) {
            invocation = (Invocation) var4.next();
            if (data.getTarget().matches(invocation)) {
                invocationsToRemove.add(invocation);
            }
        }

        var4 = invocationsToRemove.iterator();

        while (var4.hasNext()) {
            invocation = (Invocation) var4.next();
            invocations.remove(invocation);
        }

    }

    private static DefaultRegisteredInvocations getRegisteredInvocations(InvocationContainerImpl invocationContainer) {
        Field f = getRegisteredInvocationsField(invocationContainer);
        DefaultRegisteredInvocations registeredInvocations = getDefaultRegisteredInvocationsInstance(invocationContainer, f);
        return registeredInvocations;
    }

    private static Field getRegisteredInvocationsField(InvocationContainerImpl invocationContainer) {
        Field f = null;

        try {
            f = invocationContainer.getClass().getDeclaredField("registeredInvocations");
        } catch (NoSuchFieldException var4) {
            var4.printStackTrace();
        }

        f.setAccessible(true);
        return f;
    }

    private static DefaultRegisteredInvocations getDefaultRegisteredInvocationsInstance(InvocationContainerImpl invocationContainer, Field f) {
        DefaultRegisteredInvocations registeredInvocations = null;

        try {
            registeredInvocations = (DefaultRegisteredInvocations) f.get(invocationContainer);
        } catch (IllegalAccessException var5) {
            var5.printStackTrace();
        }

        return registeredInvocations;
    }

    private static List<Invocation> getInvocationsOnMock(InvocationOnMock invocationOnMock) {
        InvocationContainerImpl invocationContainer = (InvocationContainerImpl) MockUtil.getMockHandler(invocationOnMock.getMock()).getInvocationContainer();
        DefaultRegisteredInvocations registeredInvocations = getRegisteredInvocations(invocationContainer);
        Field f = getInvocationsField(registeredInvocations);
        return getInvocationsListInstance(registeredInvocations, f);
    }

    private static LinkedList<Invocation> getInvocationsListInstance(DefaultRegisteredInvocations registeredInvocations, Field f) {
        LinkedList invocations = null;

        try {
            invocations = (LinkedList) f.get(registeredInvocations);
        } catch (IllegalAccessException var5) {
            var5.printStackTrace();
        }

        return invocations;
    }

    private static Field getInvocationsField(DefaultRegisteredInvocations registeredInvocations) {
        Field f = null;

        try {
            f = registeredInvocations.getClass().getDeclaredField("invocations");
        } catch (NoSuchFieldException var4) {
            var4.printStackTrace();
        }

        f.setAccessible(true);
        return f;
    }
}
