# Configurations
All configuration MUST be set in the `buildfiles/<env>.properties` file used for compilation

- current.env = the currently executing environment
- nome.ambiente = the name of the environment
- messageSources.cacheSeconds = no more used. May be left to -1
- endpoint.url.service.core = Endpoint for the COR backend service
- endpoint.url.service.bil = Endpoint for the BIL backend service
- endpoint.url.service.fin = Endpoint for the FIN backend service
- endpoint.url.service.integ = Endpoint for the INTEG backend service
- redirect.logout.cruscotto = The redirect URL to the project dashboard
- portal.home = The portal home
- redirect.cruscotto = The redirect to the dashboard
- remincl.resource.provider = URL to the remote resources
- remincl.cache.time = caching time for the remote resources (default: 8 hours)
- sso.filter.name = Name for the SSO filter
- sso.filter.url.pattern = URL pattern for the SSO filter
    (specify a non-existing extension to prevent SSO checks)
- sso.loginHandler = fully-qualified class name for the SSO handler
- ttlCache.codifiche = TTL (Time To Live) for the cache
- jspath = the path for the local JavaScript files (for proxying support)
- jspathexternal = the path for the external JavaScript files (for proxying support)
