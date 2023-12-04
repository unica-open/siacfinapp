<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
		
		var bloccoMotivazioneAssenzaCig = $("#bloccoMotivazioneAssenzaCig");
		
		tipoDebitoSiopeVar.change(toggleMotivazioneAssenza);
		toggleMotivazioneAssenza.bind(tipoDebitoSiopeVar.filter(':checked')[0])();
		
		function toggleMotivazioneAssenza(){
			var valoreSelezionato = this ? this.value : '';
			// ':checked')[0] ORA:   this e' di tipo HTMLInputElement 
			// ':checked')    PRIMA: this era di tipo jQuery
			// HTMLInputElement.value e' una proprieta'
			// jQuery.value e' undefined
			var listaMotivazioniAssenzaCigVar = document.getElementById('listaMotivazioniAssenzaCigId');
			if(listaMotivazioniAssenzaCigVar != null){
				if(valoreSelezionato === 'Commerciale' || valoreSelezionato === 'Commerciale (con fatture)'){
					bloccoMotivazioneAssenzaCig.show();
				} else {
					bloccoMotivazioneAssenzaCig.hide();
					listaMotivazioniAssenzaCigVar.selectedIndex = 0;
					listaMotivazioniAssenzaCigVar.value = '';
				}
			}
			
		}