import { Injectable } from '@angular/core';
import { Response } from '@angular/http';
import { Subject } from 'rxjs/Subject';
import { AlertService} from 'ng-jhipster';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';

import {Principal} from '../../shared';
import { EventManager } from 'ng-jhipster';
import { ShareComponent } from './share.component';
import { Base, Modelo } from '../../entities';
import { Compartilhar, CompartilharService } from '../../entities/compartilhar/';

@Injectable()
export class ShareService {

    modalReference: NgbModalRef = null;
    private observeShares = new Subject<Compartilhar[]>();
    public shareObserver$ = this.observeShares.asObservable();

    constructor(
        private modalService: NgbModal,
        private compartilharService: CompartilharService,
        private alertService: AlertService,
        private eventManager: EventManager,
        private principal: Principal,
    ) {
        this.eventManager.subscribe('compartilharListModification', () => {
            this.consultarCompartilhamentos();
        });

        // this.aguardar();
    }

    compartilharBase(base: Base){
        this.showDialog(new Compartilhar(
            -1, //   id
            'Base', //   tipo
            'estou compartilhando a base de dados ' + base.nome + ' com vc!', //mensagem
            base.local,    // codigo
            base.nome, // nome
            0, // status
            undefined, // remetente
            undefined, // destinatario
        ));
    }

    compartilharModelo(modelo: Modelo){
        modelo.id = undefined;
        modelo.user = undefined;
        this.showDialog(new Compartilhar(
            -1, //   id
            'Modelo', //   tipo
            'estou compartilhando o modelo ' + modelo.nome + ' com vc!', //mensagem
            JSON.stringify(modelo).toString(),    // codigo
            modelo.nome, // nome
            0, // status
            undefined, // remetente
            undefined, // destinatario
        ));
    }

    showDialog(compartilhar: Compartilhar){
        if(this.modalReference){
            this.modalReference.close();
        }
        const ref: NgbModalRef = this.modalService.open(ShareComponent);
        const comp: ShareComponent = ref.componentInstance;
        comp.setCompartilhar(compartilhar);
        this.modalReference = ref;
    }

    public excluirCompartilhamento(compartilhar: Compartilhar){
        this.compartilharService.delete(compartilhar.id);
        this.consultarCompartilhamentos();
    }

    public consultarCompartilhamentos() {
        if(this.principal.isAuthenticated()) {
            this.compartilharService.query({
                page: 0,
                size: 100,
                sort: ['id']
            }).subscribe(
                (res: Response) => this.onSuccess(res.json()),
                (res: Response) => this.onError(res.json())
            );
        }
        console.log('CONSULTAR COMPARTILHAMENTOS........');
        // this.aguardar();
    }

    private aguardar() {
        // setTimeout(
        //     () => {
                this.consultarCompartilhamentos();
            // },
            // 10000);
    }

    private onSuccess(data) {
        const compartilhamentos: Compartilhar[] = data;
        this.observeShares.next(compartilhamentos);
    }

    private onError(error) {
        this.alertService.error(error.message);
    }

    public receber(compartilhar: Compartilhar){
        this.showDialog(compartilhar);
    }

}
