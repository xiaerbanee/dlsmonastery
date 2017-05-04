package net.myspring.common.cache;

import net.myspring.common.domain.IdEntity;

import javax.cache.annotation.CacheInvocationParameter;
import javax.cache.annotation.CacheKeyGenerator;
import javax.cache.annotation.CacheKeyInvocationContext;
import javax.cache.annotation.GeneratedCacheKey;
import java.lang.annotation.Annotation;

/**
 * Created by liuj on 2017/5/4.
 */
public class IdCacheKeyGenerator implements CacheKeyGenerator {
    @Override
    public GeneratedCacheKey generateCacheKey(CacheKeyInvocationContext<? extends Annotation> context) {
        final CacheInvocationParameter[] allParameters = context.getAllParameters();
        String id = ((IdEntity)allParameters[0].getValue()).getId();
        return new IdGeneratedCacheKey(id);
    }
    private static class IdGeneratedCacheKey implements GeneratedCacheKey {
        IdGeneratedCacheKey(String id) {
            this.id = id;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            return id.equals(((IdGeneratedCacheKey) obj).id);
        }

        @Override
        public int hashCode() {
            if (id == null) {
                return 0;
            }
            return id.hashCode();
        }

        @Override
        public String toString() {
            return id;
        }

        private String id;
    }
}
