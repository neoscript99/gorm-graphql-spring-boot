(window.webpackJsonp=window.webpackJsonp||[]).push([[0],{118:function(e,t,n){"use strict";Object.defineProperty(t,"__esModule",{value:!0});var r=n(204),a=function(){return function(e,t,n,r){void 0===e&&(e=null),void 0===t&&(t=null),void 0===n&&(n=null),void 0===r&&(r=null),this.pageInfo=e,this.currentItem=t,this.allList=n,this.pageList=r,e||(this.pageInfo={currentPage:1,totalCount:-1,isLastPage:!1,pageSize:10})}}();r.decorate(a,{currentItem:r.observable,allList:r.observable,pageList:r.observable,pageInfo:r.observable}),t.default=a},119:function(e,t,n){"use strict";var r=this&&this.__assign||function(){return(r=Object.assign||function(e){for(var t,n=1,r=arguments.length;n<r;n++)for(var a in t=arguments[n])Object.prototype.hasOwnProperty.call(t,a)&&(e[a]=t[a]);return e}).apply(this,arguments)},a=this&&this.__rest||function(e,t){var n={};for(var r in e)Object.prototype.hasOwnProperty.call(e,r)&&t.indexOf(r)<0&&(n[r]=e[r]);if(null!=e&&"function"===typeof Object.getOwnPropertySymbols){var a=0;for(r=Object.getOwnPropertySymbols(e);a<r.length;a++)t.indexOf(r[a])<0&&(n[r[a]]=e[r[a]])}return n},i=this&&this.__importDefault||function(e){return e&&e.__esModule?e:{default:e}};Object.defineProperty(t,"__esModule",{value:!0});var o=n(98),u=i(n(128)),l=function(){function e(e,t,n){this.domain=e,this.storeClass=t,this.domainGraphql=n,this.store=new t,this.fieldsPromise=n.getFields(u.default(e))}return e.prototype.findFirst=function(e){var t=this;void 0===e&&(e=null);return this.list({criteria:e,pageInfo:{pageSize:1,currentPage:1}}).then(function(e){return e.totalCount>0&&t.changeCurrentItem(e.results[0])})},e.prototype.listAll=function(e){var t=this;return this.list(e).then(function(e){return t.store.allList=e.results,e})},e.prototype.list=function(e){var t=this,n=e.criteria,r=void 0===n?{}:n,a=e.pageInfo,i=void 0===a?null:a,u=e.orders,l=void 0===u?this.defaultOrders:u;return i&&o.processCriteriaPage(r,i),l&&l.length>0&&o.processCriteriaOrder(r,l),this.fieldsPromise.then(function(e){return t.domainGraphql.list(t.domain,e,r)})},Object.defineProperty(e.prototype,"defaultOrders",{get:function(){return null},enumerable:!0,configurable:!0}),e.prototype.listPage=function(e){var t=this,n=e.isAppend,i=void 0!==n&&n,o=e.pageInfo,u=a(e,["isAppend","pageInfo"]);return o&&(this.store.pageInfo=o),1===this.store.pageInfo.currentPage&&(this.store.allList=[]),this.list(r({pageInfo:this.store.pageInfo},u)).then(function(e){var n=e.results,r=e.totalCount;return t.store.pageList=n,t.store.pageInfo.totalCount=r,t.store.pageInfo.isLastPage=n.length<t.store.pageInfo.pageSize||t.store.pageInfo.pageSize*t.store.pageInfo.currentPage>=r,t.store.allList=!0===i?t.store.allList.concat(n):n,e})},e.prototype.listNextPage=function(e){return this.store.pageInfo.isLastPage?"\u5df2\u7ecf\u5230\u5e95\u4e86":(this.store.pageInfo.currentPage++,this.listPage(e))},e.prototype.listFirstPage=function(e){return this.store.pageInfo.currentPage=1,this.listPage(e)},e.prototype.clearList=function(){this.store.pageList=[],this.store.allList=[]},e.prototype.changeCurrentItem=function(e){return this.store.currentItem=e,e},e.prototype.create=function(e){var t=this;return this.fieldsPromise.then(function(n){return t.domainGraphql.create(t.domain,n,e)}).then(function(e){return t.changeCurrentItem(e)})},e.prototype.update=function(e,t){var n=this;return this.fieldsPromise.then(function(r){return n.domainGraphql.update(n.domain,r,e,t)}).then(function(e){return n.changeCurrentItem(e)})},e.prototype.get=function(e){var t=this;return this.fieldsPromise.then(function(n){return t.domainGraphql.get(t.domain,n,e)}).then(function(e){return t.changeCurrentItem(e)})},e.prototype.delete=function(e){return this.domainGraphql.delete(this.domain,e)},e}();t.default=l},191:function(e,t,n){e.exports=n(375)},196:function(e,t,n){},205:function(e,t,n){"use strict";var r=this&&this.__makeTemplateObject||function(e,t){return Object.defineProperty?Object.defineProperty(e,"raw",{value:t}):e.raw=t,e},a=this&&this.__assign||function(){return(a=Object.assign||function(e){for(var t,n=1,r=arguments.length;n<r;n++)for(var a in t=arguments[n])Object.prototype.hasOwnProperty.call(t,a)&&(e[a]=t[a]);return e}).apply(this,arguments)},i=this&&this.__awaiter||function(e,t,n,r){return new(n||(n=Promise))(function(a,i){function o(e){try{l(r.next(e))}catch(t){i(t)}}function u(e){try{l(r.throw(e))}catch(t){i(t)}}function l(e){e.done?a(e.value):new n(function(t){t(e.value)}).then(o,u)}l((r=r.apply(e,t||[])).next())})},o=this&&this.__generator||function(e,t){var n,r,a,i,o={label:0,sent:function(){if(1&a[0])throw a[1];return a[1]},trys:[],ops:[]};return i={next:u(0),throw:u(1),return:u(2)},"function"===typeof Symbol&&(i[Symbol.iterator]=function(){return this}),i;function u(i){return function(u){return function(i){if(n)throw new TypeError("Generator is already executing.");for(;o;)try{if(n=1,r&&(a=2&i[0]?r.return:i[0]?r.throw||((a=r.return)&&a.call(r),0):r.next)&&!(a=a.call(r,i[1])).done)return a;switch(r=0,a&&(i=[2&i[0],a.value]),i[0]){case 0:case 1:a=i;break;case 4:return o.label++,{value:i[1],done:!1};case 5:o.label++,r=i[1],i=[0];continue;case 7:i=o.ops.pop(),o.trys.pop();continue;default:if(!(a=(a=o.trys).length>0&&a[a.length-1])&&(6===i[0]||2===i[0])){o=0;continue}if(3===i[0]&&(!a||i[1]>a[0]&&i[1]<a[3])){o.label=i[1];break}if(6===i[0]&&o.label<a[1]){o.label=a[1],a=i;break}if(a&&o.label<a[2]){o.label=a[2],o.ops.push(i);break}a[2]&&o.ops.pop(),o.trys.pop();continue}i=t.call(e,o)}catch(u){i=[6,u],r=0}finally{n=a=0}if(5&i[0])throw i[1];return{value:i[0]?i[1]:void 0,done:!0}}([i,u])}}},u=this&&this.__rest||function(e,t){var n={};for(var r in e)Object.prototype.hasOwnProperty.call(e,r)&&t.indexOf(r)<0&&(n[r]=e[r]);if(null!=e&&"function"===typeof Object.getOwnPropertySymbols){var a=0;for(r=Object.getOwnPropertySymbols(e);a<r.length;a++)t.indexOf(r[a])<0&&(n[r[a]]=e[r[a]])}return n},l=this&&this.__importDefault||function(e){return e&&e.__esModule?e:{default:e}};Object.defineProperty(t,"__esModule",{value:!0});var c,s,p,d,f,h,m=l(n(206)),g=l(n(128)),y=n(98),b=function(){function e(e,t){void 0===t&&(t={}),this.apolloClient=e,this.defaultVariables=t}return e.prototype.list=function(e,t,n){return void 0===n&&(n=null),console.debug("Graphql.list",e,n),this.apolloClient.query({query:m.default(c||(c=r(["\n                query ","ListQuery($criteria:String){\n                  ","List(criteria:$criteria){\n                        results {\n                          ","\n                        }\n                        totalCount\n                  }\n                }"],["\n                query ","ListQuery($criteria:String){\n                  ","List(criteria:$criteria){\n                        results {\n                          ","\n                        }\n                        totalCount\n                  }\n                }"])),e,e,t),fetchPolicy:"no-cache",variables:a({},this.defaultVariables,{criteria:JSON.stringify(n)})}).then(function(t){return t.data[e+"List"]})},e.prototype.get=function(e,t,n){return console.debug("Graphql.get",e,n),this.apolloClient.query({query:m.default(s||(s=r(["\n                query ","Get($id:String!){\n                  ","(id:$id){\n                    ","\n                  }\n                }"],["\n                query ","Get($id:String!){\n                  ","(id:$id){\n                    ","\n                  }\n                }"])),e,e,t),fetchPolicy:"no-cache",variables:a({},this.defaultVariables,{id:n})}).then(function(t){return t.data[e]})},e.prototype.create=function(e,t,n){var i;return console.debug("Graphql.create",e,n),this.apolloClient.mutate({mutation:m.default(p||(p=r(["\n                mutation ","CreateMutate($",":","Create){\n                  ","Create(",":$","){\n                    ","\n                  }\n                }"],["\n                mutation ","CreateMutate($",":","Create){\n                  ","Create(",":$","){\n                    ","\n                  }\n                }"])),e,e,g.default(e),e,e,e,t),fetchPolicy:"no-cache",variables:a({},this.defaultVariables,(i={},i[e]=n,i))}).then(function(t){return t.data[e+"Create"]})},e.prototype.update=function(e,t,n,i){var o;console.debug("Graphql.update",e,n,i);var l=y.pureGraphqlObject(i),c=(l.id,l.version,u(l,["id","version"]));return this.apolloClient.mutate({mutation:m.default(d||(d=r(["\n                mutation ","UpdateMutate($id:String!, $",":","Update){\n                  ","Update(id:$id, ",":$","){\n                    ","\n                  }\n                }"],["\n                mutation ","UpdateMutate($id:String!, $",":","Update){\n                  ","Update(id:$id, ",":$","){\n                    ","\n                  }\n                }"])),e,e,g.default(e),e,e,e,t),fetchPolicy:"no-cache",variables:a({},this.defaultVariables,(o={},o[e]=c,o.id=n,o))}).then(function(t){return t.data[e+"Update"]})},e.prototype.delete=function(e,t){return console.debug("Graphql.delete",e,t),this.apolloClient.mutate({mutation:m.default(f||(f=r(["\n                mutation ","DeleteMutate($id:String!){\n                  ","Delete(id:$id){\n                    success\n                    error\n                  }\n                }"],["\n                mutation ","DeleteMutate($id:String!){\n                  ","Delete(id:$id){\n                    success\n                    error\n                  }\n                }"])),e,e),fetchPolicy:"no-cache",variables:a({},this.defaultVariables,{id:t})}).then(function(t){return t.data[e+"Delete"]})},e.prototype.getFields=function(e,t,n){return void 0===t&&(t=0),void 0===n&&(n=6),i(this,void 0,void 0,function(){var r,a,i,u,l,c,s;return o(this,function(o){switch(o.label){case 0:return t>=n?[2,null]:(console.debug("Graphql.getFields",e),[4,this.getType(e)]);case 1:if(!(r=o.sent())||!r.data)return[2,null];a=r.data.__type.fields,i=[],u=0,o.label=2;case 2:return u<a.length?(l=a[u],c=null,"LIST"===l.type.kind?c=l.type.ofType.name:"OBJECT"===l.type.kind&&(c=l.type.name),c?[4,this.getFields(c,t+1,n)]:[3,4]):[3,6];case 3:return(s=o.sent())&&i.push(l.name+"{"+s+"}"),[3,5];case 4:i.push(l.name),o.label=5;case 5:return u++,[3,2];case 6:return[2,i.join(",")]}})})},e.prototype.getType=function(e){return this.apolloClient.query({query:m.default(h||(h=r(["query ","TypeQuery($type:String!){\n                  __type(name:$type){\n                    fields{\n                      name      \n                      type {\n                        kind\n                        name\n                        ofType{\n                          name\n                          kind                          \n                        }\n                      }\n                    }\n                  }\n                }"],["query ","TypeQuery($type:String!){\n                  __type(name:$type){\n                    fields{\n                      name      \n                      type {\n                        kind\n                        name\n                        ofType{\n                          name\n                          kind                          \n                        }\n                      }\n                    }\n                  }\n                }"])),e),variables:a({},this.defaultVariables,{type:e})})},e}();t.default=b},375:function(e,t,n){"use strict";n.r(t);var r,a,i=n(1),o=n.n(i),u=n(7),l=n.n(u),c=n(19),s=n(22),p=n(24),d=n(23),f=n(25),h=(n(196),n(386)),m=n(63),g=n(54),y=function(e){return o.a.createElement("div",null,"Welcome ",e.account)},b=n(385),v=function(e){function t(){var e,n;Object(c.a)(this,t);for(var r=arguments.length,a=new Array(r),i=0;i<r;i++)a[i]=arguments[i];return(n=Object(p.a)(this,(e=Object(d.a)(t)).call.apply(e,[this].concat(a)))).tableProps={loading:!1,pagination:!1},n}return Object(f.a)(t,e),Object(s.a)(t,[{key:"query",value:function(){return this.showLoading(this.domainService.listAll(this.queryParam))}},{key:"showLoading",value:function(e){var t=this;return this.tableProps.loading=!0,this.updateState(),e.finally(function(){t.tableProps.loading=!1,t.updateState()})}},{key:"componentDidMount",value:function(){this.query()}},{key:"updateState",value:function(){this.setState({tableProps:this.tableProps})}},{key:"queryParam",get:function(){return{}}}]),t}(i.Component),O=n(56),j={graphqlUri:"http://localhost:8080/graphql",dateFormat:"MM-DD",timeFormat:"YYYY-MM-DD HH:mm",asset:{logoUrl:"http://attach.neoscript.wang/tax-deduct-logo-1.1.3.jpg",cardThumbUrl:"http://attach.neoscript.wang/icon-red-diamond.png"},systemInfo:{}},P=n(187),w=n(167),S=n(168),C=n.n(S),_=n(169),q=n(53),I=n(170),k=(n(225),n(21)),E=n(118),x=(r=function(e){function t(){var e,n;Object(c.a)(this,t);for(var r=arguments.length,i=new Array(r),o=0;o<r;o++)i[o]=arguments[o];return n=Object(p.a)(this,(e=Object(d.a)(t)).call.apply(e,[this].concat(i))),Object(_.a)(n,"menuTree",a,Object(q.a)(n)),n}return Object(f.a)(t,e),t}(n.n(E).a),a=Object(I.a)(r.prototype,"menuTree",[k.j],{configurable:!0,enumerable:!0,writable:!0,initializer:function(){return{menu:{},subMenus:new Array}}}),r),M=n(119);function $(){var e=Object(w.a)(['query menuTree {\n                      menuTree(token: "','") {\n                      ',"\n                    }}"]);return $=function(){return e},e}var L,D=function(e){function t(e){var n;return Object(c.a)(this,t),(n=Object(p.a)(this,Object(d.a)(t).call(this,"menu",x,e))).menuNodeFields=void 0,n.menuNodeFields=e.getFields("MenuNode"),n}return Object(f.a)(t,e),Object(s.a)(t,[{key:"getMenuTree",value:function(e){var t=this;this.menuNodeFields.then(function(n){return t.domainGraphql.apolloClient.query({query:C()($(),e,n),fetchPolicy:"no-cache",variables:Object(P.a)({},t.domainGraphql.defaultVariables)})}).then(function(e){return t.store.menuTree=e.data.menuTree})}}]),t}(n.n(M).a),T=function(){function e(t){Object(c.a)(this,e),this.menuService=t}return Object(s.a)(e,[{key:"init",value:function(){console.log("InitService"),this.menuService.getMenuTree("gorm-dev-token")}}]),e}(),G=j.graphqlUri,U=Object(O.createApolloClient)(G),F=new O.DomainGraphql(U,{token:"gorm-dev-token"}),z=new O.DomainService("param",O.MobxDomainStore,F),A=new O.DomainService("role",O.MobxDomainStore,F),N=new D(F),V=new T(N),Y=n(171),H=n.n(Y);function J(e){return H()(e).format("YYYY-MM-DD hh:mm")}function Q(e){return e?"\u662f":"\u5426"}var B,K=[{title:"\u89d2\u8272\u540d",dataIndex:"roleName"},{title:"\u89d2\u8272\u4ee3\u7801(unique)",dataIndex:"roleCode"},{title:"\u542f\u7528",dataIndex:"enabled",render:Q},{title:"\u53ef\u7f16\u8f91",dataIndex:"editable",render:Q},{title:"\u63cf\u8ff0",dataIndex:"description"},{title:"\u4fee\u6539\u65f6\u95f4",dataIndex:"lastUpdated",render:J}],W=Object(m.a)(L=function(e){function t(){return Object(c.a)(this,t),Object(p.a)(this,Object(d.a)(t).apply(this,arguments))}return Object(f.a)(t,e),Object(s.a)(t,[{key:"render",value:function(){return o.a.createElement(b.a,Object.assign({dataSource:A.store.allList,columns:K,bordered:!0,rowKey:"id"},this.tableProps))}},{key:"domainService",get:function(){return A}},{key:"queryParam",get:function(){return{orders:[["lastUpdated","desc"]]}}}]),t}(v))||L,R=function(e){return o.a.createElement("div",null,"User ",e.account)},X=function(e){return o.a.createElement("div",null,"Note ",e.account)},Z=n(387),ee=function(e){function t(){var e,n;Object(c.a)(this,t);for(var r=arguments.length,a=new Array(r),i=0;i<r;i++)a[i]=arguments[i];return(n=Object(p.a)(this,(e=Object(d.a)(t)).call.apply(e,[this].concat(a)))).pagination={pageSize:2,onChange:n.pageChange.bind(Object(q.a)(n)),onShowSizeChange:n.pageSizeChange.bind(Object(q.a)(n)),showSizeChanger:!0,showQuickJumper:!0,showTotal:function(e){return o.a.createElement(Z.a,{color:"blue"},"\u603b\u8bb0\u5f55\u6570\uff1a",e)}},n.tableProps={loading:!1,pagination:n.pagination},n}return Object(f.a)(t,e),Object(s.a)(t,[{key:"pageChange",value:function(e,t){this.pagination.current=e,this.query()}},{key:"pageSizeChange",value:function(e,t){this.pagination.pageSize=t,this.pagination.current=1,this.query()}},{key:"query",value:function(){var e,t=this,n=z.listPage({criteria:{},pageInfo:(e=this.pagination,{currentPage:e.current,pageSize:e.pageSize,totalCount:e.total}),orders:[["lastUpdated","desc"]]}).then(function(e){return t.pagination.total=e.totalCount,e});return this.showLoading(n)}}]),t}(v),te=z.store,ne=[{title:"\u53c2\u6570\u4ee3\u7801",dataIndex:"code"},{title:"\u53c2\u6570\u540d\u79f0",dataIndex:"name"},{title:"\u53c2\u6570\u7c7b\u578b",dataIndex:"type.name"},{title:"\u53c2\u6570\u503c",dataIndex:"value"},{title:"\u4fee\u6539\u4eba",dataIndex:"lastUser.name"},{title:"\u4fee\u6539\u65f6\u95f4",dataIndex:"lastUpdated",render:J}],re=Object(m.a)(B=function(e){function t(){return Object(c.a)(this,t),Object(p.a)(this,Object(d.a)(t).apply(this,arguments))}return Object(f.a)(t,e),Object(s.a)(t,[{key:"render",value:function(){return o.a.createElement(b.a,Object.assign({dataSource:te.pageList,columns:ne,bordered:!0},this.tableProps,{rowKey:"id"}))}},{key:"domainService",get:function(){return z}}]),t}(ee))||B,ae=function(e){return o.a.createElement("div",null,"Profile ",e.account)},ie=n(125),oe=n(10),ue=n(72),le=ie.a.SubMenu;var ce,se=function(e){var t=e.rootMenu;return o.a.createElement(ie.a,{theme:"light",defaultSelectedKeys:["1"],mode:"inline"},t.subMenus.map(function(e){return function e(t){return t.menu.app?o.a.createElement(ie.a.Item,{key:t.menu.id},o.a.createElement(oe.a,{type:"file"}),o.a.createElement(ue.b,{to:"/".concat(t.menu.app,"/"),style:{display:"inline"}},t.menu.label)):o.a.createElement(le,{key:t.menu.id,title:o.a.createElement("span",null,o.a.createElement(oe.a,{type:"folder"}),t.menu.label)},t.subMenus.map(function(t){return e(t)}))}(e)}))},pe=h.a.Header,de=h.a.Content,fe=h.a.Footer,he=h.a.Sider,me=Object(m.a)(ce=function(e){function t(){var e,n;Object(c.a)(this,t);for(var r=arguments.length,a=new Array(r),i=0;i<r;i++)a[i]=arguments[i];return(n=Object(p.a)(this,(e=Object(d.a)(t)).call.apply(e,[this].concat(a)))).state={collapsed:!1},n.onCollapse=function(e){console.log(e),n.setState({collapsed:e})},n}return Object(f.a)(t,e),Object(s.a)(t,[{key:"render",value:function(){var e=N.store;return o.a.createElement(h.a,{style:{minHeight:"100vh"}},o.a.createElement(pe,null,o.a.createElement("div",{className:"logo"})),o.a.createElement(h.a,null,o.a.createElement(he,{collapsible:!0,collapsed:this.state.collapsed,onCollapse:this.onCollapse,theme:"light"},o.a.createElement(se,{rootMenu:e.menuTree})),o.a.createElement(h.a,null,o.a.createElement(de,{style:{margin:"1rem",padding:24,background:"#fff",height:"100%",minHeight:360}},o.a.createElement(g.c,null,o.a.createElement(g.a,{path:"/Role/",component:W}),o.a.createElement(g.a,{path:"/User/",component:R}),o.a.createElement(g.a,{path:"/Note/",component:X}),o.a.createElement(g.a,{path:"/Param/",component:re}),o.a.createElement(g.a,{path:"/Profile/",component:ae}),o.a.createElement(g.a,{component:y}))),o.a.createElement(fe,{style:{textAlign:"center"}},"\u7fbd\u610f\u8f6f\u4ef6 \xa92019"))))}}]),t}(i.Component))||ce,ge=function(e){return o.a.createElement("div",null,"Login ",e.account)};V.init();var ye=function(e){function t(){return Object(c.a)(this,t),Object(p.a)(this,Object(d.a)(t).apply(this,arguments))}return Object(f.a)(t,e),Object(s.a)(t,[{key:"render",value:function(){return o.a.createElement(ue.a,null,o.a.createElement(g.c,null,o.a.createElement(g.a,{path:"/",component:me}),o.a.createElement(g.a,{path:"/login/",component:ge})))}}]),t}(i.Component);Boolean("localhost"===window.location.hostname||"[::1]"===window.location.hostname||window.location.hostname.match(/^127(?:\.(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)){3}$/));l.a.render(o.a.createElement(ye,null),document.getElementById("root")),"serviceWorker"in navigator&&navigator.serviceWorker.ready.then(function(e){e.unregister()})},56:function(e,t,n){"use strict";var r=this&&this.__importDefault||function(e){return e&&e.__esModule?e:{default:e}};Object.defineProperty(t,"__esModule",{value:!0});var a=r(n(376)),i=n(380),o=n(378),u=r(n(118));t.MobxDomainStore=u.default;var l=r(n(205));t.DomainGraphql=l.default;var c=r(n(119));t.DomainService=c.default;var s=n(98);t.toFetch=s.toFetch,t.createApolloClient=function(e,t){void 0===t&&(t=null);var n=new i.BatchHttpLink(t?{uri:e,fetch:t}:{uri:e}),r=new o.InMemoryCache;return new a.default({link:n,cache:r})}},98:function(e,t,n){"use strict";var r=this&&this.__assign||function(){return(r=Object.assign||function(e){for(var t,n=1,r=arguments.length;n<r;n++)for(var a in t=arguments[n])Object.prototype.hasOwnProperty.call(t,a)&&(e[a]=t[a]);return e}).apply(this,arguments)},a=this&&this.__rest||function(e,t){var n={};for(var r in e)Object.prototype.hasOwnProperty.call(e,r)&&t.indexOf(r)<0&&(n[r]=e[r]);if(null!=e&&"function"===typeof Object.getOwnPropertySymbols){var a=0;for(r=Object.getOwnPropertySymbols(e);a<r.length;a++)t.indexOf(r[a])<0&&(n[r[a]]=e[r[a]])}return n},i=this&&this.__importDefault||function(e){return e&&e.__esModule?e:{default:e}};Object.defineProperty(t,"__esModule",{value:!0});var o=i(n(224));t.processCriteriaPage=function(e,t){return e.max=t.pageSize,e.offset=(t.currentPage-1)*t.pageSize,e},t.processCriteriaOrder=function(e,t){e.order=t.reduce(function(t,n){if("string"===typeof n||-1===n[0].indexOf("."))t.push(n);else{var r=n[0].split(".");n[0]=r[r.length-1];var a=e;r.slice(0,-1).forEach(function(e){a[e]||(a[e]={}),a=a[e]}),a.order?a.order.push(n):a.order=[n]}return t},[])},t.toFetch=function(e){var t=e.taroRequest,n=e.defaultOptions;return t?function(e,i){var o=i.body,u=a(i,["body"]);return t(r({url:e,data:o},n,u,{dataType:"txt",responseType:"text"})).then(function(e){return e.text=function(){return Promise.resolve(e.data)},e})}:null},t.pureGraphqlObject=function e(t){return t.errors=void 0,t.__typename=void 0,t.lastUpdated=void 0,t.dateCreated=void 0,Object.values(t).forEach(function(t){return o.default(t)&&e(t)}),t}}},[[191,1,2]]]);
//# sourceMappingURL=main.3ce65896.chunk.js.map