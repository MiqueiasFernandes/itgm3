import { Component, OnInit } from '@angular/core';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { AlertService } from 'ng-jhipster';
import { Modelo } from '../../modelo/';
import { Projeto } from '../../projeto/';
import { Cenario } from '../../cenario/';
import { Base, BaseService } from '../../base/';
import { ModeloExclusivo, ModeloExclusivoService } from '../';

@Component({
  selector: 'jhi-mapear-modelo',
  templateUrl: './mapear-modelo.component.html',
    styleUrls: ['./mapear-modelo.scss']
})
export class MapearModeloComponent implements OnInit {

    modeloExclusivo: ModeloExclusivo;
    vars = null;
    campos: string[] = [];
    visivel = [];
    titulos: string[] = [];
    palpite = '';

    constructor(
        private alertService: AlertService,
        public activeModal: NgbActiveModal,
        private baseService: BaseService,
        private modeloExclusivoService: ModeloExclusivoService,
    ) {
        this.modeloExclusivo = new ModeloExclusivo();
    }

    ngOnInit() {
    }

    setModelo(modelo: Modelo) {
        if (!modelo) {
            this.close();
        }
        this.modeloExclusivo.modelo = modelo;
        this.vars = modelo.variaveis.split(',');
        let i = 0;
        this.vars.forEach(() => {
            this.visivel[i] = false;
            this.titulos[i++] = 'selecione...';
        });
        this.palpite = modelo.palpite;
    }

    adicionar(event) {
        const strs: string[] = event.target.value.split(',');
        for (let i = 0; i < strs.length - 1; i++) {
            this.addCampo(strs[i]);
        }
    }

    private onError(error) {
        this.alertService.error(error.message);
    }

    close() {
        this.activeModal.dismiss('closed');
    }

    closeDropdown() {
        for (let k = 0 ; k < this.visivel.length; k++) {
            this.visivel[k] =  false;
        }
    }

    toogleDropdow(i) {
        this.closeDropdown();
        this.visivel[i] = !this.visivel[i];
    }

    selecionar(i, j) {
        this.titulos[i] = this.campos[j];
        this.visivel[i] = false;
    }

    getTop(i: number) {
        return ( (50 * i) + 160 ) + 'px';
    }

    setCenario(cenario: Cenario) {
        if (!cenario) {
            this.close();
        }
        this.modeloExclusivo.cenario = cenario;
    }

    setProjeto(projeto: Projeto) {
        this.baseService.getBasesByProjeto(projeto)
            .subscribe(
                (bases: Base[]) => {
                    bases.forEach((base: Base) => {
                        this.baseService.getFieldsOfBase(base).subscribe(
                            (res: string[]) => {
                                res.forEach((campo) => {
                                    this.addCampo(campo);
                                });
                            }
                        );
                    });
                },
                (error) => {
                    this.onError(error.json());
                }
            );
    }

    addCampo(campo: string) {
        if (this.campos.indexOf(campo) < 0) {
            this.campos.push(campo);
        }
    }

    mapearModelo() {
        this.modeloExclusivo.id = undefined;
        let map = '';
        let i = 0;
        this.vars.forEach((variavel: string) => {
            map += variavel + '=' + this.titulos[i++] + ',';
        });
        this.modeloExclusivo.mapeamento =  map.substr(0, map.length - 1);
        this.modeloExclusivoService.create(this.modeloExclusivo).subscribe(() => {
            this.close();
        });
    }

}
