import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { EventManager, AlertService, JhiLanguageService } from 'ng-jhipster';

import { Base } from './base.model';
import { BasePopupService } from './base-popup.service';
import { BaseService } from './base.service';
import { Projeto, ProjetoService } from '../projeto';

@Component({
    selector: 'jhi-base-dialog',
    templateUrl: './base-dialog.component.html'
})
export class BaseDialogComponent implements OnInit {

    base: Base;
    authorities: any[];
    isSaving: boolean;

    projetos: Projeto[];
    constructor(
        public activeModal: NgbActiveModal,
        private jhiLanguageService: JhiLanguageService,
        private alertService: AlertService,
        private baseService: BaseService,
        private projetoService: ProjetoService,
        private eventManager: EventManager
    ) {
        this.jhiLanguageService.setLocations(['base']);
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
        if (this.base.id !== undefined) {
            this.baseService.update(this.base)
                .subscribe((res: Base) =>
                    this.onSaveSuccess(res), (res: Response) => this.onSaveError(res));
        } else {
            this.baseService.create(this.base)
                .subscribe((res: Base) =>
                    this.onSaveSuccess(res), (res: Response) => this.onSaveError(res));
        }
    }

    private onSaveSuccess(result: Base) {
        this.eventManager.broadcast({ name: 'baseListModification', content: 'OK'});
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
    selector: 'jhi-base-popup',
    template: ''
})
export class BasePopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private basePopupService: BasePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.modalRef = this.basePopupService
                    .open(BaseDialogComponent, params['id']);
            } else {
                this.modalRef = this.basePopupService
                    .open(BaseDialogComponent);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
