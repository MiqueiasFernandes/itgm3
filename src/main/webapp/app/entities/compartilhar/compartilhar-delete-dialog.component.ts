import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { EventManager, JhiLanguageService } from 'ng-jhipster';

import { Compartilhar } from './compartilhar.model';
import { CompartilharPopupService } from './compartilhar-popup.service';
import { CompartilharService } from './compartilhar.service';

@Component({
    selector: 'jhi-compartilhar-delete-dialog',
    templateUrl: './compartilhar-delete-dialog.component.html'
})
export class CompartilharDeleteDialogComponent {

    compartilhar: Compartilhar;

    constructor(
        private jhiLanguageService: JhiLanguageService,
        private compartilharService: CompartilharService,
        public activeModal: NgbActiveModal,
        private eventManager: EventManager
    ) {
        this.jhiLanguageService.setLocations(['compartilhar']);
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.compartilharService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'compartilharListModification',
                content: 'Deleted an compartilhar'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-compartilhar-delete-popup',
    template: ''
})
export class CompartilharDeletePopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private compartilharPopupService: CompartilharPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.modalRef = this.compartilharPopupService
                .open(CompartilharDeleteDialogComponent, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
