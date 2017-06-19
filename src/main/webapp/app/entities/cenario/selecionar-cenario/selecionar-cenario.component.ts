import { Component, OnInit } from '@angular/core';

import { NgbActiveModal, NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { AlertService } from 'ng-jhipster';
import { Response } from '@angular/http';
import { Cenario, CenarioService, FabAddCenarioComponent, } from '../';
import { Customize, CustomizeService } from "../../customize/";

@Component({
    selector: 'jhi-selecionar-cenario',
    templateUrl: './selecionar-cenario.component.html',
    styleUrls: ['./selecionar-cenario.scss']
})
export class SelecionarCenarioComponent implements OnInit {

    cenarios: Cenario[];
    visivel = false;
    titulo = 'Selecione um cenario...';
    cenario: Cenario = null;

    constructor(
        private alertService: AlertService,
        private cenarioService: CenarioService,
        private customizeService: CustomizeService,
        public activeModal: NgbActiveModal,
        private modalService: NgbModal,
    ) { }

    ngOnInit() {
        this.loadAll();
    }

    loadAll() {
        this.customizeService.getCustomize()
            .subscribe(
                (customize: Customize) => {
                    if (customize && customize.projeto) {
                        this.cenarioService
                            .getCenariosByProjeto(customize.projeto)
                            .subscribe(
                                (cenarios: Cenario[]) => this.onSuccess(cenarios),
                                (res: Response) => this.onError(res.json())
                            );
                    }
                }
            );
    }

    private onSuccess(cenarios) {
        this.cenarios = cenarios;

        if (cenarios.length === 0) {
            this.onError({message: 'Crie um cenario primeiro!'});
            return;
        }

        this.customizeService.getCustomize()
            .subscribe(
                (customize: Customize) => {
                    if (customize && customize.cenario) {
                        this.selecionar(customize.cenario);
                    } else {
                        this.selecionar(cenarios[0]);
                    }
                });
    }

    private onError(error) {
        this.alertService.error(error.message);
    }

    toogleDropdown() {
        this.visivel = !this.visivel;
    }

    close() {
        this.activeModal.dismiss('closed');
    }

    alterarCenario() {
        if (!this.cenario) {
            this.alertService.warning('selecione um cenario!');
        }else {
            this.customizeService.customizeCenario(this.cenario);
            this.close();
        }
    }

    selecionar(cenario: Cenario) {
        this.titulo = cenario.nome;
        this.cenario = cenario;
    }

    adicionarCenario() {
        this.close();
        this.modalService.open(FabAddCenarioComponent);
    }

}
