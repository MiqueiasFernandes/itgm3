import { Component, OnInit } from '@angular/core';

import {NgbActiveModal, NgbModal, NgbModalRef} from '@ng-bootstrap/ng-bootstrap';
import { AlertService } from 'ng-jhipster';
import { Response } from '@angular/http';
import { Projeto, ProjetoService, FabAddProjetoComponent } from '../';
import { Customize, CustomizeService } from '../../customize'

@Component({
  selector: 'jhi-selecionar-projeto',
  templateUrl: './selecionar-projeto.component.html',
    styleUrls: ['./selecionar-projeto.scss']
})
export class SelecionarProjetoComponent implements OnInit {

    projetos: Projeto[];
    visivel = false;
    titulo = 'Selecione um projeto...';
    projeto: Projeto = null;

    constructor(
        private alertService: AlertService,
        private projetoService: ProjetoService,
        public activeModal: NgbActiveModal,
        private modalService: NgbModal,
        private customizeService: CustomizeService,
    ) { }

    ngOnInit() {
        this.loadAll();
    }

    loadAll() {
        this.projetoService.query({
            page: 0,
            size: 100,
            sort: ['id']}).subscribe(
            (res: Response) => this.onSuccess(res.json()),
            (res: Response) => this.onError(res.json())
        );
    }

    private onSuccess(data) {
        this.projetos = data;

        if (data.length === 0) {
            this.onError({message: 'Crie um projeto primeiro!'});
            return;
        }

        this.customizeService.getCustomize().subscribe(
            (customize: Customize) => {
                if(customize && customize.projeto){
                    this.selecionar(customize.projeto)
                }else{
                    this.selecionar(this.projetos[0]);
                }
            }
        );
    }

    private onError(error) {
        this.alertService.error(error.message);
    }

    toogleDropdown() {
        this.visivel = !this.visivel;
    }

    close() {
        this.activeModal.close(this.projeto);
    }

    alterarProjeto() {
        if (!this.projeto) {
            this.alertService.warning('selecione um projeto!');
        }else {
            this.customizeService.customizeProjeto(this.projeto);
            this.close();
        }
    }

    selecionar(projeto: Projeto) {
        this.titulo = projeto.nome;
        this.projeto = projeto;
    }

    adicionarProjeto(){
        const ref: NgbModalRef = this.modalService.open(FabAddProjetoComponent);
        ref.result.then((projeto: Projeto) => {
            this.projeto = projeto;
            this.close();
        });
    }

}
