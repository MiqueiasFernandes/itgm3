import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { EventManager, AlertService, JhiLanguageService } from 'ng-jhipster';

import { Prognose } from './prognose.model';
import { PrognosePopupService } from './prognose-popup.service';
import { PrognoseService } from './prognose.service';
import { Base, BaseService } from '../base';
import { ModeloExclusivo, ModeloExclusivoService } from '../modelo-exclusivo';

@Component({
    selector: 'jhi-prognose-dialog',
    templateUrl: './prognose-dialog.component.html'
})
export class PrognoseDialogComponent implements OnInit {

    prognose: Prognose;
    authorities: any[];
    isSaving: boolean;

    bases: Base[];

    modeloexclusivos: ModeloExclusivo[];
    constructor(
        public activeModal: NgbActiveModal,
        private jhiLanguageService: JhiLanguageService,
        private alertService: AlertService,
        private prognoseService: PrognoseService,
        private baseService: BaseService,
        private modeloExclusivoService: ModeloExclusivoService,
        private eventManager: EventManager
    ) {
        this.jhiLanguageService.setLocations(['prognose']);
    }

    ngOnInit() {
        this.isSaving = false;
        this.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
        this.baseService.query().subscribe(
            (res: Response) => { this.bases = res.json(); }, (res: Response) => this.onError(res.json()));
        this.modeloExclusivoService.query().subscribe(
            (res: Response) => { this.modeloexclusivos = res.json(); }, (res: Response) => this.onError(res.json()));
    }
    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.prognose.id !== undefined) {
            this.prognoseService.update(this.prognose)
                .subscribe((res: Prognose) =>
                    this.onSaveSuccess(res), (res: Response) => this.onSaveError(res));
        } else {
            this.prognoseService.create(this.prognose)
                .subscribe((res: Prognose) =>
                    this.onSaveSuccess(res), (res: Response) => this.onSaveError(res));
        }
    }

    private onSaveSuccess(result: Prognose) {
        this.eventManager.broadcast({ name: 'prognoseListModification', content: 'OK'});
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

    trackBaseById(index: number, item: Base) {
        return item.id;
    }

    trackModeloExclusivoById(index: number, item: ModeloExclusivo) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-prognose-popup',
    template: ''
})
export class PrognosePopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private prognosePopupService: PrognosePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.modalRef = this.prognosePopupService
                    .open(PrognoseDialogComponent, params['id']);
            } else {
                this.modalRef = this.prognosePopupService
                    .open(PrognoseDialogComponent);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
