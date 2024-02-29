config.files.push({
   pattern: __dirname + "/src/**/resources/**",
   watched: false,
   included: false,
   served: true,
   nocache: false
});
config.set({
    "proxies": {
       "/": __dirname + "/"
    },
    "urlRoot": "/__karma__/"
});
