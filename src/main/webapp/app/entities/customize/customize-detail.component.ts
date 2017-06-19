import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { EventManager , JhiLanguageService , DataUtils } from 'ng-jhipster';

import { Customize } from './customize.model';
import { CustomizeService } from './customize.service';

@Component({
    selector: 'jhi-customize-detail',
    templateUrl: './customize-detail.component.html'
})
export class CustomizeDetailComponent implements OnInit, OnDestroy {

    customize: Customize;
    private subscription: any;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: EventManager,
        private jhiLanguageService: JhiLanguageService,
        private dataUtils: DataUtils,
        private customizeService: CustomizeService,
        private route: ActivatedRoute
    ) {
        this.jhiLanguageService.setLocations(['customize']);
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInCustomizes();
    }

    load(id) {
        this.customizeService.find(id).subscribe((customize) => {
            this.customize = customize;
        });
    }
    byteSize(field) {
        return this.dataUtils.byteSize(field);
    }

    openFile(contentType, field) {
        return this.dataUtils.openFile(contentType, field);
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInCustomizes() {
        this.eventSubscriber = this.eventManager.subscribe('customizeListModification', (response) => this.load(this.customize.id));
    }
}
