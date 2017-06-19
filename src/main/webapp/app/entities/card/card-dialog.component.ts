import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { EventManager, AlertService, JhiLanguageService } from 'ng-jhipster';

import { Card } from './card.model';
import { CardPopupService } from './card-popup.service';
import { CardService } from './card.service';
import { Cenario, CenarioService } from '../cenario';

@Component({
    selector: 'jhi-card-dialog',
    templateUrl: './card-dialog.component.html'
})
export class CardDialogComponent implements OnInit {

    card: Card;
    authorities: any[];
    isSaving: boolean;

    cenarios: Cenario[];
    constructor(
        public activeModal: NgbActiveModal,
        private jhiLanguageService: JhiLanguageService,
        private alertService: AlertService,
        private cardService: CardService,
        private cenarioService: CenarioService,
        private eventManager: EventManager
    ) {
        this.jhiLanguageService.setLocations(['card']);
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
        if (this.card.id !== undefined) {
            this.cardService.update(this.card)
                .subscribe((res: Card) =>
                    this.onSaveSuccess(res), (res: Response) => this.onSaveError(res));
        } else {
            this.cardService.create(this.card)
                .subscribe((res: Card) =>
                    this.onSaveSuccess(res), (res: Response) => this.onSaveError(res));
        }
    }

    private onSaveSuccess(result: Card) {
        this.eventManager.broadcast({ name: 'cardListModification', content: 'OK'});
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
    selector: 'jhi-card-popup',
    template: ''
})
export class CardPopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private cardPopupService: CardPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.modalRef = this.cardPopupService
                    .open(CardDialogComponent, params['id']);
            } else {
                this.modalRef = this.cardPopupService
                    .open(CardDialogComponent);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
