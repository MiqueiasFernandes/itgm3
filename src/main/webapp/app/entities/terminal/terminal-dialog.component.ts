import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { EventManager, AlertService, JhiLanguageService } from 'ng-jhipster';

import { Terminal } from './terminal.model';
import { TerminalPopupService } from './terminal-popup.service';
import { TerminalService } from './terminal.service';
import { Cenario, CenarioService } from '../cenario';

@Component({
    selector: 'jhi-terminal-dialog',
    templateUrl: './terminal-dialog.component.html'
})
export class TerminalDialogComponent implements OnInit {

    terminal: Terminal;
    authorities: any[];
    isSaving: boolean;

    cenarios: Cenario[];
    constructor(
        public activeModal: NgbActiveModal,
        private jhiLanguageService: JhiLanguageService,
        private alertService: AlertService,
        private terminalService: TerminalService,
        private cenarioService: CenarioService,
        private eventManager: EventManager
    ) {
        this.jhiLanguageService.setLocations(['terminal']);
    }

    ngOnInit() {
        this.isSaving = false;
        this.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
        this.cenarioService.query().subscribe(
            (res: Response) => { this.cenarios = res.json(); }, (res: Response) => this.onError(res.json()));
    }
    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.terminal.id !== undefined) {
            this.terminalService.update(this.terminal)
                .subscribe((res: Terminal) =>
                    this.onSaveSuccess(res), (res: Response) => this.onSaveError(res));
        } else {
            this.terminalService.create(this.terminal)
                .subscribe((res: Terminal) =>
                    this.onSaveSuccess(res), (res: Response) => this.onSaveError(res));
        }
    }

    private onSaveSuccess(result: Terminal) {
        this.eventManager.broadcast({ name: 'terminalListModification', content: 'OK'});
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

    trackCenarioById(index: number, item: Cenario) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-terminal-popup',
    template: ''
})
export class TerminalPopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private terminalPopupService: TerminalPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.modalRef = this.terminalPopupService
                    .open(TerminalDialogComponent, params['id']);
            } else {
                this.modalRef = this.terminalPopupService
                    .open(TerminalDialogComponent);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
