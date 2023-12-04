// JavaScript Document

<!--
		var setting = {
			check: {
				enable: true,
				chkStyle: "radio",
				radioType: "level"
			},
			data: {
				simpleData: {
					enable: true
				}
			}
						
		};


		var struttAmm =[
			{ id:1, pId:0, name:"Direzione 1"},
			{ id:11, pId:1, name:"Settore 1"},			
			{ id:111, pId:11, name:"Ufficio 1"},
			{ id:112, pId:11, name:"Ufficio 2"},
			{ id:12, pId:1, name:"Settore 2"},
			{ id:121, pId:12, name:"ufficio 1"},						
			{ id:2, pId:0, name:"Direzione 2 "},
			{ id:22, pId:2, name:"Settore 1"},
			{ id:221, pId:22, name:"Ufficio 1"},
			{ id:222, pId:22, name:"Ufficio 2"},
			{ id:223, pId:22, name:"Ufficio 3"},
			{ id:224, pId:22, name:"Ufficio 4"},			
			{ id:23, pId:2, name:"Settore 2"},
			{ id:231, pId:23, name:"Ufficio 1"},
			{ id:232, pId:23, name:"Ufficio 2"},
			{ id:233, pId:23, name:"Ufficio 3"},
			{ id:234, pId:23, name:"Ufficio 4"},
		];
		
		var struttProposte =[
			{ id:1, pId:0, name:"Direzione 1"},
			{ id:11, pId:1, name:"Settore 1"},			
			{ id:12, pId:1, name:"Settore 2"},						
			{ id:2, pId:0, name:"Direzione 2 "},
			{ id:22, pId:2, name:"Settore 1"},		
			{ id:23, pId:2, name:"Settore 2"},
			{ id:3, pId:0, name:"Direzione 3 "},
			{ id:33, pId:3, name:"Settore 1"},		
			{ id:34, pId:3, name:"Settore 2"},
			{ id:35, pId:3, name:"Settore 2"},
			{ id:4, pId:0, name:"Direzione 4 "},
			{ id:44, pId:4, name:"Settore 1"},		
			{ id:45, pId:4, name:"Settore 2"},
		];
		var pdc =[
			{ id:1, pId:0, name:"Entrate correnti di natura tributaria,  contributiva e perequativa"},
			{ id:11, pId:1, name:"Tributi"},
			
			{ id:111, pId:11, name:"Tributi diretti"},
			{ id:1111, pId:111, name:"Altre imposte sostitutive n.a.c."},
			{ id:11111, pId:1111, name:"Altre imposte sostitutive n.a.c. riscosse in via ordinaria e attraverso altre forme"},
			{ id:11112, pId:1111, name:"Altre imposte sostitutive n.a.c. riscosse a mezzo ruoli"},
			
			{ id:112, pId:11, name:"Tributi indiretti"},
			{ id:1121, pId:112, name:"Imposta sul gas metano"},
			{ id:11211, pId:1121, name:"Imposta sul gas metano riscossa in via ordinaria e attraverso altre forme"},
			{ id:11211, pId:1121, name:"Imposta sul gas metano riscossa a mezzo ruoli"},
			
			{ id:1122, pId:112, name:"Imposte sulle concessioni dei beni del demanio e del patrimonio indisponibile"},
			{ id:11221, pId:1122, name:"Imposte sulle concessioni dei beni del demanio e del patrimonio indisponibile riscosse in via ordinaria e attraverso altre forme"},
			{ id:11221, pId:1122, name:"Imposte sulle concessioni dei beni del demanio e del patrimonio indisponibile riscosse a mezzo ruoli"},
			
			
			{ id:12, pId:1, name:"Fondi perequativi"},
			{ id:121, pId:12, name:"Fondi perequativi da Amministrazioni Centrali"},
			{ id:1221, pId:121, name:"Fondi perequativi dallo Stato"},
			{ id:1221, pId:121, name:"Fondo perequativo dallo Stato - Sanità"},
			
			{ id:2, pId:0, name:"Trasferimenti correnti"},

			{ id:22, pId:2, name:"Trasferimenti correnti"},
			{ id:221, pId:22, name:"Trasferimenti correnti da Amministrazioni pubbliche"},
			{ id:2221, pId:221, name:"Trasferimenti correnti da Amministrazioni Centrali"},
			{ id:22221, pId:2221, name:"Trasferimenti correnti da Ministeri"},			
			{ id:22221, pId:2221, name:"Trasferimenti correnti da Stato - Fondo Sanitario Nazionale - finanziamento del Servizio sanitario nazionale"},
			
			
		
		];
		
		var code;		
		function setCheck() {
			var type = $("#level").attr("checked")? "level":"all";
			setting.check.radioType = type;
		
			showCode('setting.check.radioType = "' + type + '";');
			$.fn.zTree.init($(".treePDC"), setting, pdc);
			$.fn.zTree.init($(".treeStruttAmm"), setting, struttAmm);
			$.fn.zTree.init($(".treePRO"), setting, struttProposte);
			
			$.fn.zTree.init($("#treePDC"), setting, pdc);
			$.fn.zTree.init($("#treeStruttAmm"), setting, struttAmm);
			$.fn.zTree.init($("#treePRO"), setting, struttProposte);
		}
		function showCode(str) {
			if (!code) code = $("#code");
			code.empty();
			code.append("<li>"+str+"</li>");
		}
		
		$(document).ready(function(){
			setCheck();			
			$("#level").bind("change", setCheck);
			$("#all").bind("change", setCheck);
		});
		//-->