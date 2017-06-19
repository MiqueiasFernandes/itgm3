import { Component, OnInit } from '@angular/core';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { AlertService } from 'ng-jhipster';
import { Modelo, ModeloService } from '../';
import { Principal, User, UserService, Account } from '../../../shared';

@Component({
  selector: 'jhi-fab-add-modelo',
  templateUrl: './fab-add-modelo.component.html',
  styles: []
})
export class FabAddModeloComponent implements OnInit {

    modelo: Modelo;
    peloCodigo = false;

    constructor(
        public activeModal: NgbActiveModal,
        private userService: UserService,
        private principal: Principal,
        private modeloService: ModeloService,
        private alertService: AlertService

    ) {
        this.modelo = new Modelo();
    }

    ngOnInit() {
        this.principal.identity().then((account) => {
            this.extrairUsuario(account);
        });
    }

    private extrairUsuario(account: Account) {
        this.userService
            .getUser(account)
            .subscribe(
                (user: User) => {
                    this.modelo.user = user;
                },
                (error) => {
                    this.onError(error.json());
                });
    }

    private onError(error) {
        this.alertService.error(error.message);
    }

    private close() {
        this.activeModal.dismiss('closed');
    }

    criarModelo() {
        if(this.peloCodigo && !this.modelo.nome){
            this.modelo.nome = 'modeloSemNomePeloCodigo';
        }
        this.modeloService.create(this.modelo).subscribe(
            (modelo: Modelo) => {
                this.close();
            },
            (error) => this.onError(error)
        );
    }

    alterar(){
        this.peloCodigo = !this.peloCodigo;
    }

    limpar(){
        if(this.peloCodigo){
            this.modelo.codigo = '';
        }else{
            this.modelo.nome =
                this.modelo.formula =
                    this.modelo.funcao =
                        this.modelo.variaveis =
                            this.modelo.palpite =
                                this.modelo.parametros =
                                    this.modelo.requires = '';
        }
    }

}
