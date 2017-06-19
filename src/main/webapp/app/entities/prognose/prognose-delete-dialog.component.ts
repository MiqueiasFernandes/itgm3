import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { EventManager, JhiLanguageService } from 'ng-jhipster';

import { Prognose } from './prognose.model';
import { PrognosePopupService } from './prognose-popup.service';
import { PrognoseService } from './prognose.service';

@Component({
    selector: 'jhi-prognose-delete-dialog',
    templateUrl: './prognose-delete-dialog.component.html'
})
export class PrognoseDeleteDialogComponent {

    prognose: Prognose;

    constructor(
        private jhiLanguageService: JhiLanguageService,
        private prognoseService: PrognoseService,
        public activeModal: NgbActiveModal,
        private eventManager: EventManager
    ) {
        this.jhiLanguageService.setLocations(['prognose']);
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.prognoseService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'prognoseListModification',
                content: 'Deleted an prognose'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-prognose-delete-popup',
    template: ''
})
export class PrognoseDeletePopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private prognosePopupService: PrognosePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.modalRef = this.prognosePopupService
                .open(PrognoseDeleteDialogComponent, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
