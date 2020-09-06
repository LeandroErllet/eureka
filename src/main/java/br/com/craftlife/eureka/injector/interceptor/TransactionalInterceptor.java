package br.com.craftlife.eureka.injector.interceptor;

import br.com.craftlife.eureka.database.DatabaseManager;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

public class TransactionalInterceptor implements MethodInterceptor {

    private final DatabaseManager databaseManager;

    public TransactionalInterceptor(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        boolean iniciado = false;
        EntityManager entityManager = databaseManager.getEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        if (!transaction.isActive()) {
            iniciado = true;
            transaction.begin();
        }
        try {
            Object returnValue = invocation.proceed();
            if (iniciado) {
                transaction.commit();
            }
            return returnValue;
        } catch (Exception exc) {
            try {
                if (transaction.isActive()) {
                    transaction.rollback();
                }
            } catch (Exception ignore) {}
            throw exc;
        }
    }

}
