import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { EventManager, AlertService, JhiLanguageService } from 'ng-jhipster';

import { Compartilhar } from './compartilhar.model';
import { CompartilharPopupService } from './compartilhar-popup.service';
import { CompartilharService } from './compartilhar.service';
import { User, UserService } from '../../shared';

@Component({
    selector: 'jhi-compartilhar-dialog',
    templateUrl: './compartilhar-dialog.component.html'
})
export class CompartilharDialogComponent implements OnInit {

    compartilhar: Compartilhar;
    authorities: any[];
    isSaving: boolean;

    users: User[];
    constructor(
        public activeModal: NgbActiveModal,
        private jhiLanguageService: JhiLanguageService,
        private alertService: AlertService,
        private compartilharService: CompartilharService,
        private userService: UserService,
        private eventManager: EventManager
    ) {
        this.jhiLanguageService.setLocations(['compartilhar']);
    }

    ngOnInit() {
        this.isSaving = false;
        this.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
        this.userService.query().subscribe(
            (res: Response) => { this.users = res.json(); }, (res: Response) => this.onError(res.json()));
    }
    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.compartilhar.id !== undefined) {
            this.compartilharService.update(this.compartilhar)
                .subscribe((res: Compartilhar) =>
                    this.onSaveSuccess(res), (res: Response) => this.onSaveError(res));
        } else {
            this.compartilharService.create(this.compartilhar)
                .subscribe((res: Compartilhar) =>
                    this.onSaveSuccess(res), (res: Response) => this.onSaveError(res));
        }
    }

    private onSaveSuccess(result: Compartilhar) {
        this.eventManager.broadcast({ name: 'compartilharListModification', content: 'OK'});
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

    trackUserById(index: number, item: User) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-compartilhar-popup',
    template: ''
})
export class CompartilharPopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private compartilharPopupService: CompartilharPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.modalRef = this.compartilharPopupService
                    .open(CompartilharDialogComponent, params['id']);
            } else {
                this.modalRef = this.compartilharPopupService
                    .open(CompartilharDialogComponent);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
