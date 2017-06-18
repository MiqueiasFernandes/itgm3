import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { EventManager, AlertService, JhiLanguageService } from 'ng-jhipster';

import { Cenario } from './cenario.model';
import { CenarioPopupService } from './cenario-popup.service';
import { CenarioService } from './cenario.service';
import { Projeto, ProjetoService } from '../projeto';

@Component({
    selector: 'jhi-cenario-dialog',
    templateUrl: './cenario-dialog.component.html'
})
export class CenarioDialogComponent implements OnInit {

    cenario: Cenario;
    authorities: any[];
    isSaving: boolean;

    projetos: Projeto[];
    constructor(
        public activeModal: NgbActiveModal,
        private jhiLanguageService: JhiLanguageService,
        private alertService: AlertService,
        private cenarioService: CenarioService,
        private projetoService: ProjetoService,
        private eventManager: EventManager
    ) {
        this.jhiLanguageService.setLocations(['cenario']);
    }

    ngOnInit() {
        this.isSaving = false;
        this.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
        this.projetoService.query().subscribe(
            (res: Response) => { this.projetos = res.json(); }, (res: Response) => this.onError(res.json()));
    }
    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.cenario.id !== undefined) {
            this.cenarioService.update(this.cenario)
                .subscribe((res: Cenario) =>
                    this.onSaveSuccess(res), (res: Response) => this.onSaveError(res));
        } else {
            this.cenarioService.create(this.cenario)
                .subscribe((res: Cenario) =>
                    this.onSaveSuccess(res), (res: Response) => this.onSaveError(res));
        }
    }

    private onSaveSuccess(result: Cenario) {
        this.eventManager.broadcast({ name: 'cenarioListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError(error) {
        try {
            error.json();
        } catch (exception) {
            error.message = error.text();
        }
        this.isSaving = false;
        this.onError(error);
    }

    private onError(error) {
        this.alertService.error(error.message, null, null);
    }

    trackProjetoById(index: number, item: Projeto) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-cenario-popup',
    template: ''
})
export class CenarioPopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private cenarioPopupService: CenarioPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.modalRef = this.cenarioPopupService
                    .open(CenarioDialogComponent, params['id']);
            } else {
                this.modalRef = this.cenarioPopupService
                    .open(CenarioDialogComponent);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
