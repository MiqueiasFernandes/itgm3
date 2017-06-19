import { Component } from '@angular/core';

import { NgbActiveModal,NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { AlertService } from 'ng-jhipster';
import { Compartilhar, CompartilharService } from "../../entities/compartilhar/";
import { Account, Principal } from '../../shared';
import { UserService } from "../../shared/user/user.service";
import { User } from "../../shared/user/user.model";
import {
    Projeto,
    SelecionarProjetoComponent,
    Base,
    BaseService,
    Modelo,
    ModeloService,
    Customize,
    CustomizeService} from '../../entities/'

@Component({
    selector: 'jhi-share',
    templateUrl: './share.component.html',
    styles: []
})
export class ShareComponent {

    compartilharEl: Compartilhar = null;
    destinatario = '';

    constructor(
        private principal: Principal,
        public activeModal: NgbActiveModal,
        private modalService: NgbModal,
        private alertService: AlertService,
        private compartilharService: CompartilharService,
        private userService: UserService,
        private baseService: BaseService,
        private modeloService: ModeloService,
        private customizeService: CustomizeService,
    ) {
        this.compartilharEl = new Compartilhar(
            -1, //   id
            '', //   tipo
            '', //   mensagem
            '', //   codigo
            '', //   nome
            0,  //   status
            null, // remetente
            null, // destinatario
        );
    }

    public setCompartilhar(compartilhar: Compartilhar) {
        this.compartilharEl = compartilhar;
        if(!this.isCompartilhando()) {
            this.destinatario = compartilhar.remetente.login;
        }
    }

    private compartilhar() {
        this.principal.identity().then((account: Account) => {
            this.userService.find(account.login).subscribe(
                (remetente: User) => {
                    this.userService.find(this.destinatario).subscribe(
                        (destinatario: User) => {
                            this.compartilharEl.remetente = remetente;
                            this.compartilharEl.destinatario = destinatario;
                            this.compartilharEl.status = 1;
                            this.compartilharEl.id = undefined;

                            this.compartilharService
                                .create(this.compartilharEl)
                                .subscribe(
                                    () => {
                                        this.close();
                                    },
                                    () => {
                                        this.onError({message : 'Houve um erro verifique.'});
                                    });
                        });
                });
        });
    }

    private isCompartilhando():boolean {
        return (!this.compartilharEl.id || this.compartilharEl.id === -1);
    }

    private receber() {
        this.receberBase(this.compartilharEl);
        this.receberModelo(this.compartilharEl);
    }

    private rejeitar() {
        this.compartilharService.delete(this.compartilharEl.id)
            .subscribe(
                () => {
                    this.close();
                },
                (error) => {
                    this.onError(error.json())
                }
            );
    }

    private onError(error) {
        this.alertService.error(error.message);
    }

    private close() {
        this.activeModal.dismiss('closed');
    }


    private  receberBase(compartilhar: Compartilhar) {
        alert('recebendo base ');
        if(compartilhar.tipo === 'Base') {
            alert('recebendo e');
            this.customizeService.getCustomize().subscribe(
                (customize: Customize) => {
                    if ( customize.projeto && customize.projeto.id ) {
                        ////caso tem projeto definido
                        alert('ATENÇÂO: A base será definida para este projeto ativo: '
                            + customize.projeto.nome + ' (' + customize.projeto.id + ')');
                        this.criarBase(compartilhar.nome, customize.projeto);
                    } else {
                        ///caso nao tem projeto definido
                        const ref: NgbModalRef =  this.modalService.open(SelecionarProjetoComponent);
                        ref.result.then((projeto: Projeto) => {
                            this.criarBase(compartilhar.nome, projeto);
                        });
                    }
                }
            );
        }
    }

    private receberModelo(compartilhar: Compartilhar){
        if (compartilhar.tipo === 'Modelo') {
            const modelo: Modelo= JSON.parse(compartilhar.codigo);
            modelo.id = undefined;
            modelo.user = compartilhar.destinatario;
            this.modeloService.create(modelo)
                .subscribe(
                    () => {
                        this.compartilharService.delete(compartilhar.id)
                            .subscribe(
                                () => {
                                    this.close();
                                }
                            );
                    }
                );
        }
    }

    private criarBase(nome: string, projeto: Projeto) {

        this.baseService.create(
            new Base(undefined, nome, undefined, projeto))
            .subscribe(
                (base: Base) => {
                    this.compartilharService
                        .receberCompartilhamento(this.compartilharEl, base.local)
                        .subscribe(
                            (comp: Compartilhar) => {
                                this.compartilharService.delete(comp.id)
                                    .subscribe(
                                        () => {
                                            this.close();
                                        }
                                    );
                            },
                            (error) => {
                                this.onError(error.json())
                            }
                        );
                }
            );

    }

}
